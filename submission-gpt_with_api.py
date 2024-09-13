# -*- coding: utf-8 -*-


# 라이브러리 임포트 및 버전 설정

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


install_package('openai', '0.28.1')



import time, json, os
import boto3
from collections import defaultdict
import openai
openai.api_key = "openai-api-key"


GPT_MODEL_NAME = "gpt-4"  # 사용할 GPT 모델 명
INPUT_JSON_PATH = './result.json'  
OUTPUT_JSON_PATH = './result_v2.json' 


#****************************************************************** GPT 프롬프트 
# GPT API에 요청할 프롬프트 미리 정의 (사용자 지정 가능)
DEFAULT_PROMPT_TEMPLATE = (
    f"사진을 보고 이 사진에 대한 아래의 데이터를 줘 : \n\n"
    f"파일명 : {{file_name}}\n"
    f"등장한 인물 : {{custom_labels}}\n"
    f"감정 : {{scene_labels}}\n"
    f"상황 : {{circumstances_summary}}\n\n"
    f"아래의 정보를 알려줘 : \n"
    f"1. 사진과 관련된 해시태그 두 개 (단, 해시태그에는 인물의 이름(레이블)이 들어가면 안 됨)\n"
    f"2. 해당 사진을 설명하는 간단한 구 (단, 인물 이름은 들어가도 되는데, 문장이 아니라 단어들의 집합인 구여야 함. 그리고 MZ스러운 젊은 말투로 표현해야함)\n"
    f"3. 무조건 한국말로 표현해줘. 어떤 경우라도 알파벳은 사용하지 마 \n"
    f"4. 비속어 절대 사용 금지 (존나 라는 말 사용 금지)\n"
    f"5. 결과값에 번호를 매기지 말아줘\n"
    f"6. 문장 끝에 쓰이는 점은 사용하지 마\n"
    f"7. 상황에 어울리는 이모지도 넣어줘\n"
    f"8. 문장은 공백 포함 30자 미만\n"
    f"예시 : \n"
    f"#해시태그1 #해시태그2\n"
    f"누구(인물이름-레이블)와 함께 인생네컷 한 장 넘 행복한 하루"
)
#******************************************************************





"""""""""""""""""""""""""""""""""""""""""
GPT api 프롬프트 사용
"""""""""""""""""""""""""""""""""""""""""
# GPT API에 요청 보내기
def generate_gpt4_response(prompt, model_name=GPT_MODEL_NAME):
    try:
        response = openai.ChatCompletion.create(
            model=model_name,
            messages=[
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": prompt}
            ],
            max_tokens=150,
            n=1,
            stop=None,
            temperature=0.8
        )
        return response['choices'][0]['message']['content']
    except Exception as e:
        return f"Error: {str(e)}"

# JSON 파일 불러오기
def load_json_data(file_path):
    if os.path.exists(file_path):
        with open(file_path, 'r', encoding='utf-8') as json_file:
            try:
                return json.load(json_file)
            except json.JSONDecodeError:
                return []
    return []

# JSON 데이터를 프롬프트로 변환
def create_prompt_from_json(image_data, prompt_template=DEFAULT_PROMPT_TEMPLATE):
    file_name = image_data['file_name']
    custom_labels = ', '.join(image_data['custom_labels'])
    scene_labels = ', '.join(image_data.get('scene_labels', []))

    # circumstances 분석
    circumstances = image_data.get('circumstances', {})
    relevant_circumstances = []
    for category, details in circumstances.items():
        if details:
            for detail in details:
                relevant_circumstances.append(f"{category}: {', '.join(detail['relevant_labels'])}")

    circumstances_summary = ', '.join(relevant_circumstances)

    # 프롬프트 생성
    prompt = prompt_template.format(
        file_name=file_name,
        custom_labels=custom_labels,
        scene_labels=scene_labels,
        circumstances_summary=circumstances_summary
    )
    return prompt

# GPT 응답을 JSON 파일에 저장
def save_response_to_json(file_name, hashtags, description, output_file=OUTPUT_JSON_PATH):
    data = load_json_data(output_file)

    updated = False
    for item in data:
        if item['file_name'] == file_name:
            item['hashtags'] = hashtags
            item['description'] = description
            updated = True
            break

    if not updated:
        data.append({
            'file_name': file_name,
            'hashtags': hashtags,
            'description': description,
        })

    # JSON 파일에 저장 (ensure_ascii=False로 설정하여 한글이 깨지지 않게 함)
    with open(output_file, 'w', encoding='utf-8') as json_file:
        json.dump(data, json_file, indent=4, ensure_ascii=False)

    # 수정된 프린트문
    print(f"'{file_name}'이(가) {output_file}에 저장되었습니다.")

# 불필요한 항목들을 제거하고 필요한 정보만 남기는 함수
def clean_json_data(input_file, output_file):
    data = load_json_data(input_file)

    cleaned_data = []
    for item in data:
        cleaned_item = {
            "file_name": item["file_name"],
            "custom_labels": item["custom_labels"],
            "hashtags": item.get("hashtags", ""),
            "description": item.get("description", "")
        }
        cleaned_data.append(cleaned_item)

    # 결과 저장
    with open(output_file, 'w', encoding='utf-8') as json_file:
        json.dump(cleaned_data, json_file, indent=4, ensure_ascii=False)

    print(f"필요한 정보만 남긴 데이터가 {output_file}에 저장되었습니다.")

# 메인 함수
def main():
    # 기존 JSON 데이터 로드
    json_data = load_json_data(INPUT_JSON_PATH)

    # 모든 이미지에 대해 GPT API 호출
    for image_data in json_data:
        prompt = create_prompt_from_json(image_data)
        response = generate_gpt4_response(prompt)

        # 응답에서 해시태그와 설명 분리
        lines = response.split('\n')
        hashtags = lines[0]  # 첫 번째 줄이 해시태그
        description = lines[1]  # 두 번째 줄이 설명

        # JSON 파일에 해시태그와 설명 저장
        save_response_to_json(image_data['file_name'], hashtags, description)

    # 불필요한 정보 제거 후 새로운 JSON 파일 생성
    clean_json_data(OUTPUT_JSON_PATH, OUTPUT_JSON_PATH)

if __name__ == "__main__":
    main()




