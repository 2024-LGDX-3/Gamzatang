const sliderFrame = document.querySelector('.slider-frame');
let currentIndex = 0;

function showNextSlide() {
    currentIndex = (currentIndex + 1) % 3; // Assuming 3 slides
    sliderFrame.style.transform = `translateX(${-currentIndex * 300}px)`; // Adjust based on slide width
}

// Automatically change slides every 5 seconds
setInterval(showNextSlide, 3000);

// Redirect to WebOS.html on any click
document.body.addEventListener('click', function() {
    window.location.href = 'templates/osHome.html';
});
document.addEventListener('DOMContentLoaded', function() {
    const slides = document.querySelectorAll('.slide');
    const pastelColors = ['#f9cdd5', '#cdebf9', '#d9f9c5', '#f6d6ff', '#e0f4f7', '#f7e7d9']; // 파스텔 색상 배열

    slides.forEach((slide, index) => {
        slide.style.backgroundColor = pastelColors[index % pastelColors.length];
    });
});
