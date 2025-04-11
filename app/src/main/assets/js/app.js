function showSection(sectionId, element) {
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    
    document.getElementById(sectionId).classList.add('active');
    
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
    });
    element.classList.add('active');

    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

let touchStartX = 0;
let touchEndX = 0;

document.addEventListener('touchstart', e => {
    touchStartX = e.changedTouches[0].screenX;
});

document.addEventListener('touchend', e => {
    touchEndX = e.changedTouches[0].screenX;
    handleSwipe();
});

function handleSwipe() {
    const sections = ['lifestyle', 'nutrition', 'activity', 'mental'];
    const SWIPE_THRESHOLD = 50;
    
    const activeSection = document.querySelector('.section.active');
    const currentIndex = sections.indexOf(activeSection.id);
    
    if (touchEndX < touchStartX - SWIPE_THRESHOLD && currentIndex < sections.length - 1) {
        const nextSection = sections[currentIndex + 1];
        const nextNav = document.querySelector(`[href="#${nextSection}"]`);
        showSection(nextSection, nextNav);
    } else if (touchEndX > touchStartX + SWIPE_THRESHOLD && currentIndex > 0) {
        const prevSection = sections[currentIndex - 1];
        const prevNav = document.querySelector(`[href="#${prevSection}"]`);
        showSection(prevSection, prevNav);
    }
} 