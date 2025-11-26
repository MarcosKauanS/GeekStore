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

    const closeViewModal = () => viewModal.style.display = "none";

    // Não existe botão "Adicionar Pedido", apenas visualizar/editar

    document.querySelectorAll(".edit").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            const id = btn.getAttribute("data-id");

            formOrderId.value = id || "";
            formStatus.value = btn.getAttribute("data-status");

            editModalOverlay.style.display = "flex";
        });
    });

    document.querySelectorAll(".view").forEach(btn => {
        btn.addEventListener("click", (event) => {
            event.preventDefault();

            const id = btn.getAttribute("data-id");

            viewId.textContent = id;
            viewUser.textContent = btn.getAttribute("data-user");
            viewDate.textContent = btn.getAttribute("data-date");
            viewTotal.textContent = "R$ " + btn.getAttribute("data-total");
            viewStatus.textContent = btn.getAttribute("data-status");

            btnViewEdit.onclick = () => {
                closeViewModal();
                formOrderId.value = (id && id !== "undefined") ? id : "";
                formStatus.value = btn.getAttribute("data-status");
                editModalOverlay.style.display = "flex";
            };

            viewModal.style.display = "flex";
        });
    });
});