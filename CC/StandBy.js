document.addEventListener('DOMContentLoaded', function() {
    const selectedImages = JSON.parse(localStorage.getItem('selectedImages')) || [];
    const sliderFrame = document.querySelector('.slider-frame');

    selectedImages.forEach((imgSrc, index) => {
        const slide = document.createElement('div');
        slide.classList.add('slide');
        
        const img = document.createElement('img');
        img.src = imgSrc;
        img.alt = `Selected Photo ${index + 1}`;

        const caption = document.createElement('p');
        caption.innerHTML = `#Selected Photo ${index + 1} <br><strong>추억의 사진</strong>`;

        slide.appendChild(img);
        slide.appendChild(caption);
        sliderFrame.appendChild(slide);
    });

    // 슬라이드가 추가된 후에 너비를 가져오기 위해 약간의 지연을 추가
    setTimeout(() => {
        const slideWidth = document.querySelector('.slide').offsetWidth; // 슬라이드 너비 가져오기

        const slides = document.querySelectorAll('.slide');
        const pastelColors = ['#f9cdd5', '#cdebf9', '#d9f9c5', '#f6d6ff', '#e0f4f7', '#f7e7d9'];

        slides.forEach((slide, index) => {
            slide.style.backgroundColor = pastelColors[index % pastelColors.length];
        });

        let currentIndex = 0;

        function showNextSlide() {
            currentIndex = (currentIndex + 1) % selectedImages.length; // 슬라이드 순환
            sliderFrame.style.transform = `translateX(${-currentIndex * slideWidth}px)`; // 슬라이드 너비에 맞춰 변환
        }

        // 매 2초마다 자동으로 슬라이드 변경
        setInterval(showNextSlide, 2000);

        document.querySelector('.slider-container').addEventListener('click', function() {
            window.location.href = '/CC/WebOS.html';
        });
    }, 100); // 100ms 지연 후 실행
});