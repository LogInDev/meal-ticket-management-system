/**
 * 중식, 석식 활성화 탭 라인 적용
 */
function updateActiveLine() {
    const activeTab = document.querySelector('.tab-button.active');
    const activeLine = document.querySelector('.active-line');

    if (activeTab && activeLine) {
        const tabLeft = activeTab.offsetLeft; // 활성 탭의 왼쪽 위치
        activeLine.style.left = `${tabLeft}px`; // 활성화된 탭 위치로 이동
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // 초기 설정: 활성 라인을 현재 활성 탭으로 이동
    updateActiveLine();

    // 탭 클릭 시 활성화된 탭과 라인을 업데이트
    const tabs = document.querySelectorAll('.tab-button');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(tab => tab.classList.remove('active')); // 모든 탭 비활성화
            tab.classList.add('active'); // 클릭된 탭 활성화
            updateActiveLine(); // 활성 라인 업데이트
        });
    });
});

function updateRecord(button) {
    const userId = button.getAttribute('data-user-id');
    const mealType = button.getAttribute('data-meal-type');
    const isCanceled = button.getAttribute('data-is-canceled') === 'true';

    axios.post('/api/mealRecord/updateRecord', {
        userId: userId,
        mealType: mealType,
        isCanceled: isCanceled
    })
        .then(response => {
            if (response.status === 200) {
                alert("식권 대장 수정 완료!");
                window.location.reload(); // 페이지를 새로고침하여 변경사항 반영
            }
        })
        .catch(error => {
            console.error("Error updating meal record:", error);
        });
}

