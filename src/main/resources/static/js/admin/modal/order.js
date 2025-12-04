document.addEventListener("DOMContentLoaded", () => {
    const viewModal = document.getElementById("viewModalOverlay");
    const editModalOverlay = document.getElementById("modalOverlay");

    const viewId = document.getElementById("viewId");
    const viewUser = document.getElementById("viewUser");
    const viewDate = document.getElementById("viewDate");
    const viewTotal = document.getElementById("viewTotal");
    const viewStatus = document.getElementById("viewStatus");
    const btnViewEdit = document.getElementById("btnViewEdit");

    const formOrderId = document.getElementById("orderId");
    const formStatus = document.getElementById("status");

    const closeViewModal = () => { if (viewModal) viewModal.style.display = "none"; };

    document.querySelectorAll(".edit").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            if (formOrderId) formOrderId.value = btn.getAttribute("data-id");
            if (formStatus) formStatus.value = btn.getAttribute("data-status");
            if (editModalOverlay) editModalOverlay.style.display = "flex";
        });
    });

    document.querySelectorAll(".view").forEach(btn => {
        btn.addEventListener("click", (event) => {
            event.preventDefault();
            if (viewId) viewId.textContent = "#" + btn.getAttribute("data-id");
            if (viewUser) viewUser.textContent = btn.getAttribute("data-user");
            if (viewDate) viewDate.textContent = btn.getAttribute("data-date");
            if (viewTotal) viewTotal.textContent = "R$ " + btn.getAttribute("data-total");

            if (viewStatus) {
                const st = btn.getAttribute("data-status");
                viewStatus.textContent = st;
                viewStatus.className = "view-text status " + st; // Aplica cor
            }

            if (btnViewEdit) {
                btnViewEdit.onclick = () => {
                    closeViewModal();
                    if (formOrderId) formOrderId.value = btn.getAttribute("data-id");
                    if (formStatus) formStatus.value = btn.getAttribute("data-status");
                    if (editModalOverlay) editModalOverlay.style.display = "flex";
                };
            }
            if (viewModal) viewModal.style.display = "flex";
        });
    });
});