document.addEventListener('DOMContentLoaded', () => {
    const sliderContent = document.querySelector('.slider-content');
    const sliderFrame = document.querySelector('.slider-frame');
    const imageWidth = sliderFrame.offsetWidth; // sliderFrame의 너비를 이미지 너비로 설정
    let currentIndex = 0;
    const imageDisplayTime = 5000; // 5초 동안 이미지 표시
    let slideInterval; // 슬라이드 간격을 제어하는 변수
    let isFullscreen = false; // 전체화면 여부 상태

    // 서버에서 이미지 데이터를 가져오기
    fetch('/demo/getImageTextData') // 이미지 데이터를 가져옴
        .then(response => response.json())
        .then(images => {
            if (images.length === 0) {
                console.log("No images found.");
                return; // 이미지가 없을 경우 슬라이더 설정을 중지
            }

            // 서버에서 받아온 이미지를 슬라이더에 추가
            images.forEach(imageData => {
                // 슬라이드 컨테이너 생성
                const slideContainer = document.createElement('div');
                slideContainer.className = 'slide';  // 슬라이드 개별 항목

                // 이미지 엘리먼트 생성
                const imgElement = document.createElement('img');
                imgElement.src = imageData.imageUrl;
                imgElement.alt = 'Sliding Image';

                // 슬라이드 컨테이너에 이미지 추가
                slideContainer.appendChild(imgElement);

                // 슬라이드 컨텐츠에 슬라이드 컨테이너 추가
                sliderContent.appendChild(slideContainer);
            });

            const totalImages = images.length;
            sliderContent.style.width = `${imageWidth * totalImages}px`; // 전체 슬라이드 너비 설정

            // 슬라이드 이동 함수 (한 장씩 이동)
            function showNextImage() {
                currentIndex++;
                if (currentIndex < totalImages) {
                    sliderContent.style.transform = `translateX(${-currentIndex * imageWidth}px)`; // 슬라이드 이동
                } else {
                    sliderContent.style.transform = `translateX(0px)`; // 첫 번째 사진으로 돌아가기
                    currentIndex = 0;
                }
            }

            // 슬라이드 시작
            startSlider();

            function startSlider() {
                slideInterval = setInterval(showNextImage, imageDisplayTime); // 자동 슬라이드
            }

            function stopSlider() {
                clearInterval(slideInterval);
            }

            sliderFrame.addEventListener('mouseenter', stopSlider); // 마우스 오버 시 슬라이드 멈추기
            sliderFrame.addEventListener('mouseleave', startSlider); // 마우스 떠날 시 슬라이드 다시 시작

            // 전체화면 기능
            document.querySelectorAll('.slide img').forEach(image => {
                image.addEventListener('click', () => {
                    if (!isFullscreen) {
                        const fullscreenDiv = document.createElement('div');
                        fullscreenDiv.classList.add('fullscreen');
                        fullscreenDiv.appendChild(image.cloneNode()); // 이미지 복사본을 추가
                        document.body.appendChild(fullscreenDiv);

                        fullscreenDiv.addEventListener('click', () => {
                            document.body.removeChild(fullscreenDiv); // 전체 화면 종료
                            isFullscreen = false;
                        });
                        isFullscreen = true; // 전체 화면 모드 활성화
                    }
                });
            });
        })
        .catch(error => console.error('Error fetching images:', error));
});