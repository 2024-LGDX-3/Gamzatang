document.addEventListener('DOMContentLoaded', () => {
    const sliderContent = document.querySelector('.slider-content');
    const images = document.querySelectorAll('.slider-content img');
    const imageWidth = 660; // 이미지 너비
    let currentIndex = 0;
    const imageDisplayTime = 5000; // 5초 동안 이미지 표시
    const totalImages = images.length;

    // slider-content의 너비를 이미지 개수에 맞게 설정
    sliderContent.style.width = `${imageWidth * totalImages}px`;

    // 슬라이드 이동 함수
    function showNextImage() {
        currentIndex++;
        if (currentIndex < totalImages - 1) {
            sliderContent.style.transform = `translateX(${-currentIndex * imageWidth}px)`; // 슬라이드 이동
        } else {
            // 마지막 이미지 (복사된 첫 번째 이미지)로 이동한 후, 첫 번째 이미지로 바로 이동
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

    // 일정 시간마다 이미지 전환
    setInterval(showNextImage, imageDisplayTime);
});