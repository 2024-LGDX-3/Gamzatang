document.addEventListener('DOMContentLoaded', () => {
    const sliderContent = document.querySelector('.slider-content');
    const sliderFrame = document.querySelector('.slider-frame');
    const imageWidth = 660; // 이미지 너비
    let currentIndex = 0;
    const imageDisplayTime = 5000; // 5초 동안 이미지 표시
    let slideInterval; // 슬라이드 간격을 제어하는 변수
    let isFullscreen = false; // 전체화면 여부 상태

    // localStorage에서 선택된 이미지 가져오기
    const selectedImages = JSON.parse(localStorage.getItem('selectedImages') || '[]');

    if (selectedImages.length === 0) {
        console.log("No images selected.");
        return; // 선택된 이미지가 없을 경우, 슬라이드 설정을 중지
    }

    // 선택된 이미지를 슬라이더에 추가
    selectedImages.forEach(imageUrl => {
        const imgElement = document.createElement('img');
        imgElement.src = imageUrl;
        imgElement.alt = 'Sliding Image';
        sliderContent.appendChild(imgElement);
    });

    const images = document.querySelectorAll('.slider-content img');
    const totalImages = images.length;

    // slider-content의 너비를 이미지 개수에 맞게 설정
    sliderContent.style.width = `${imageWidth * totalImages}px`;

    // 슬라이드 이동 함수
    function showNextImage() {
        currentIndex++;
        if (currentIndex < totalImages - 1) {
            sliderContent.style.transform = `translateX(${-currentIndex * imageWidth}px)`; // 슬라이드 이동
        } else {
            sliderContent.style.transform = `translateX(${-currentIndex * imageWidth}px)`; // 복사된 첫 번째 이미지로 이동
            setTimeout(() => {
                sliderContent.style.transition = 'none'; // 애니메이션 제거
                currentIndex = 0; // 첫 번째 이미지로 설정
                sliderContent.style.transform = `translateX(0)`; // 첫 번째 이미지로 이동
                setTimeout(() => {
                    sliderContent.style.transition = 'transform 1s ease-in-out'; // 애니메이션 다시 설정
                }, 50); // 짧은 시간 후에 애니메이션 다시 적용
            }, 1000); // 마지막 슬라이드 애니메이션 끝나고 실행
        }
    }

    // 슬라이드 시작 함수
    function startSlider() {
        slideInterval = setInterval(showNextImage, imageDisplayTime);
    }

    // 슬라이드 중지 함수
    function stopSlider() {
        clearInterval(slideInterval);
    }

    // 마우스 오버 시 슬라이드 멈추기
    sliderFrame.addEventListener('mouseenter', stopSlider);

    // 마우스 떠날 시 슬라이드 다시 시작
    sliderFrame.addEventListener('mouseleave', startSlider);

    // 전체화면 기능
    images.forEach(image => {
        image.addEventListener('click', () => {
            if (!isFullscreen) {
                // 전체 화면 모드 적용
                const fullscreenDiv = document.createElement('div');
                fullscreenDiv.classList.add('fullscreen');
                fullscreenDiv.appendChild(image.cloneNode()); // 이미지 복사본을 추가하여 원본이 아닌 복사본을 조작
                document.body.appendChild(fullscreenDiv);

                // 전체 화면 상태에서 다시 클릭 시 원래 상태로 복귀
                fullscreenDiv.addEventListener('click', () => {
                    document.body.removeChild(fullscreenDiv); // 전체 화면 종료
                    isFullscreen = false; // 전체 화면 모드 해제
                });
                isFullscreen = true; // 전체 화면 모드 활성화
            }
        });
    });

    // 슬라이드를 처음 시작
    startSlider();
});
