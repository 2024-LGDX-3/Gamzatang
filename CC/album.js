let selectedImages = [];
    
function toggleSelect(frameElement) {
    frameElement.classList.toggle('selected');
    const imgElement = frameElement.querySelector('img');
    const imgSrc = imgElement.src;

    if (selectedImages.includes(imgSrc)) {
        selectedImages = selectedImages.filter(src => src !== imgSrc);
    } else {
        selectedImages.push(imgSrc);
    }

    // 선택한 이미지 경로를 콘솔에 출력
    console.log('Selected Images:', selectedImages);
}

function uploadSelection() {
    localStorage.setItem('selectedImages', JSON.stringify(selectedImages)); // 이미지 경로들을 로컬 스토리지에 저장
    location.href = 'StandBy.html'; // index1.html로 이동
}