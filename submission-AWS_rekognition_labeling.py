import subprocess
import sys



def install_package(package_name, version=None):
    try:
        if version:
            __import__(package_name)
        else:
            __import__(package_name)
    except ImportError:
        if version:
            subprocess.check_call([sys.executable, "-m", "pip", "install", f"{package_name}=={version}"])
        else:
            subprocess.check_call([sys.executable, "-m", "pip", "install", package_name])


install_package('boto3')


import time, json, os, io
import boto3
from PIL import Image
from collections import defaultdict
import openai
openai.api_key = "openai-api-key"




os.environ['AWS_ACCESS_KEY_ID'] = 'aws-access-key-id'
os.environ['AWS_SECRET_ACCESS_KEY'] = 'aws-secret-access-key'
os.environ['AWS_DEFAULT_REGION'] = 'region'


def s3_connection():
    try:
        s3 = boto3.client("s3") 
    except Exception as e:
        print(e)
    else:
        print("S3 bucket Connected")
        return s3

def rekognition_connection():
    try:
        rekognition = boto3.client("rekognition")  
    except Exception as e:
        print(e)
    else:
        print("Rekognition Connected")
        return rekognition

s3 = s3_connection()
rekognition = rekognition_connection()







"""""""""""""""""""""""""""""""""""""""""
Turn On AWS Rekognition Custom Label
"""""""""""""""""""""""""""""""""""""""""
min_inference_units = 1
min_confidence = 20
base_photo_path = './content'  # local path
# output_file = os.path.join(os.path.dirname(photo_label_dict['file_name']), "result.json")  # local output path


project_arn = 'rekognition-project-anr'
model_arn = 'rekognition-model-arn'
version_name = 'rekognition-version-name'



def start_model(project_arn, model_arn, version_name, min_inference_units):
    try:
        print('Starting model: ' + model_arn)
        response=rekognition.start_project_version(ProjectVersionArn=model_arn, MinInferenceUnits=min_inference_units)

        project_version_running_waiter = rekognition.get_waiter('project_version_running')
        project_version_running_waiter.wait(ProjectArn=project_arn, VersionNames=[version_name])

        describe_response=rekognition.describe_project_versions(ProjectArn=project_arn, VersionNames=[version_name])
        for model in describe_response['ProjectVersionDescriptions']:
            print("Status: " + model['Status'])
            print("Message: " + model['StatusMessage'])
    except Exception as e:
        print(e)
    print('Done...')

def main():
    start_model(project_arn, model_arn, version_name, min_inference_units)

if __name__ == "__main__":
    main()





"""""""""""""""""""""""""""""""""""""""""
Get the information of pictures and save it to json 
"""""""""""""""""""""""""""""""""""""""""

def get_photo_list(directory):
    supported_formats = ('.jpg', '.jpeg', '.png')  
    return [f for f in os.listdir(directory) if f.lower().endswith(supported_formats)]

def resize_image_if_needed(image_path, max_size=5 * 1024 * 1024):
    with open(image_path, 'rb') as img_file:
        img = Image.open(img_file)
        img_byte_arr = io.BytesIO()
        img.save(img_byte_arr, format=img.format)

        if img_byte_arr.tell() > max_size:
            print(f"이미지 크기가 5MB를 초과합니다. 건너뜁니다: {image_path}")
            return None
        
        return img_byte_arr.getvalue()
    

# Detect Emotion
def detect_faces_and_emotions(local_photo_path):
    resized_image = resize_image_if_needed(local_photo_path)  
    if resized_image is None:
        return []  

    response = rekognition.detect_faces(
        Image={'Bytes': resized_image},
        Attributes=['ALL']
    )

    emotions = [
        emotion['Type'] for face_detail in response['FaceDetails']
        for emotion in face_detail.get('Emotions', [])
        if emotion['Confidence'] > 50
    ]
    return emotions

# Detect Circumstance
def detect_labels(local_photo_path):
    resized_image = resize_image_if_needed(local_photo_path)
    if resized_image is None:
        return []  

    response = rekognition.detect_labels(
        Image={'Bytes': resized_image},
        MaxLabels=10, 
        MinConfidence=50 
    )
    return [label['Name'] for label in response['Labels']]

# Detect Person and Circumstance at once
def show_custom_and_detect_labels(model, local_photo_path, min_confidence):
    resized_image = resize_image_if_needed(local_photo_path)
    if resized_image is None:
        return None 

    response = rekognition.detect_custom_labels(
        Image={'Bytes': resized_image},
        MinConfidence=min_confidence,
        ProjectVersionArn=model
    )

    custom_labels = []  
    if 'CustomLabels' in response:
        print(f"Custom Label 중 {len(response['CustomLabels'])}명이 탐지되었습니다. : {local_photo_path}:")
        for idx, customLabel in enumerate(response['CustomLabels']):
            # print(f"❣️Label {idx+1}: {customLabel['Name']}")
            custom_labels.append(customLabel['Name'])
    else:
        print("커스텀 레이블 인물 중 아무도 감지되지 않았습니다.")

    scene_labels = detect_labels(local_photo_path)
    emotion_labels = detect_faces_and_emotions(local_photo_path)
    # print(f"→ 감지된 Scene Labels : {scene_labels}")
    # print(f"→ 감지된 Emotion Labels : {emotion_labels}")    

    return {
        'file_name': local_photo_path,
        'custom_labels': custom_labels,
        'scene_labels': scene_labels + emotion_labels 
    }



# Theme Labeling
# sorry for messy function lol
def group_photos_by_theme(image_data):
    theme_dict = {
        'Family': [], 'Trip': [], 'Meal': [], 'Selfie': [], 'Indoor': [],
        'Holding Glass': [], 'Day': [], 'Night': [], 'Animals': [],
        'Entertainment': [], 'Sports': [], 'lgedx': []
    }
    for label in image_data['scene_labels'] + image_data['custom_labels']:
        if label in ['Travel', 'Beach', 'Outdoor', 'Park', 'Tree', 'Nature']:
            theme_dict['Trip'].append({'relevant_labels': [label]})
        elif label in ['Meal', 'Food', 'Plate']:
            theme_dict['Meal'].append({'relevant_labels': [label]})
        elif label == 'Selfie':
            theme_dict['Selfie'].append({'relevant_labels': [label]})
        elif label in ['Indoor', 'Table', 'Chair']:
            theme_dict['Indoor'].append({'relevant_labels': [label]})
        elif label in ['Glass', 'Cup']:
            theme_dict['Holding Glass'].append({'relevant_labels': [label]})
        elif label in ['Animal', 'Dog', 'Cat', 'Bird']:
            theme_dict['Animals'].append({'relevant_labels': [label]})
        elif label == 'Baby':
            theme_dict['Family'].append({'relevant_labels': [label]})
        elif label in ['Amusement Park', 'Shopping Mall', 'Sport']:
            theme_dict['Entertainment'].append({'relevant_labels': [label]})
        elif label == 'Night':
            theme_dict['Night'].append({'relevant_labels': [label]})
        else:
            theme_dict['Day'].append({'relevant_labels': [label]})
    return theme_dict

def create_circumstances(image_data):
    modified_file_name = 'diary/' + os.path.basename(image_data['file_name'])
    return {
        'file_name': modified_file_name,
        'custom_labels': image_data['custom_labels'],
        'scene_labels': image_data['scene_labels'],
        'circumstances': group_photos_by_theme(image_data)
    }

def save_dict_to_json(photo_label_dict):
    output_file = "result.json"

    if os.path.exists(output_file):
        with open(output_file, 'r') as json_file:
            try:
                existing_data = json.load(json_file)
                if not isinstance(existing_data, list):
                    raise ValueError("Invalid data format in JSON.")
            except (json.JSONDecodeError, ValueError):
                existing_data = []
    else:
        existing_data = []

    new_file_name = os.path.basename(photo_label_dict['file_name'])

    updated = False
    for idx, data in enumerate(existing_data):
        existing_file_name = os.path.basename(data['file_name'])        
        if existing_file_name == new_file_name:
            existing_data[idx] = photo_label_dict
            updated = True
            break

    if not updated:
        existing_data.append(photo_label_dict)

    with open(output_file, 'w') as json_file:
        json.dump(existing_data, json_file, indent=4)

    print(f"\n이미지 정보가 '{output_file}'에 추가 또는 업데이트되었습니다.")


def analyze_multiple_photos():
    photo_list = get_photo_list(base_photo_path)  
    for photo in photo_list:
        local_photo_path = os.path.join(base_photo_path, photo)
        photo_info = show_custom_and_detect_labels(model_arn, local_photo_path, min_confidence)
        if photo_info is None:
            continue 
        result = create_circumstances(photo_info)
        save_dict_to_json(result)
    print(f"\n각 사진에 대한 정보들이 저장되었습니다.")


if __name__ == "__main__":
    analyze_multiple_photos()







"""""""""""""""""""""""""""""""""""""""""
MUST DO : Turn Off AWS Rekognition Custom Label model
"""""""""""""""""""""""""""""""""""""""""
def stop_model(model_arn):
    print('Stopping model: ' + model_arn)
    try:
        response = rekognition.stop_project_version(ProjectVersionArn=model_arn)
        status = response['Status']
        print('Status: ' + status)
    except Exception as e:
        print(e)
    print(f'Model Stopped and Done...\n')

def main():
    stop_model(model_arn)

if __name__ == "__main__":
    main()


