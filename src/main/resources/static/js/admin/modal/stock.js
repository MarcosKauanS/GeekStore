document.addEventListener("DOMContentLoaded", () => {
    const viewModal = document.getElementById("viewModalOverlay");
    const editModalOverlay = document.getElementById("modalOverlay");

    const viewId = document.getElementById("viewId");
    const viewProductName = document.getElementById("viewProductName");
    const viewQuantity = document.getElementById("viewQuantity");
    const btnViewEdit = document.getElementById("btnViewEdit");
    const btnViewDelete = document.getElementById("btnViewDelete");

    const formId = document.getElementById("stockId");
    const formProduct = document.getElementById("product");
    const formQuantity = document.getElementById("quantity");
    const modalTitle = document.getElementById("modalTitle");
    const openModalBtn = document.getElementById("openModal");

    const closeViewModal = () => viewModal.style.display = "none";

    if (openModalBtn) {
        openModalBtn.addEventListener("click", () => {
            formId.value = "";
            formProduct.value = "";
            formQuantity.value = "";
            modalTitle.textContent = "Adicionar Estoque";
            editModalOverlay.style.display = "flex";
        });
    }

    document.querySelectorAll(".edit").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            const id = btn.getAttribute("data-id");

            formId.value = id || "";
            formProduct.value = btn.getAttribute("data-product");
            formQuantity.value = btn.getAttribute("data-quantity");

            modalTitle.textContent = "Editar Estoque";
            editModalOverlay.style.display = "flex";
        });
    });

    document.querySelectorAll(".view").forEach(btn => {
        btn.addEventListener("click", (event) => {
            event.preventDefault();

            const id = btn.getAttribute("data-id");
            const productId = btn.getAttribute("data-product-id");
            const productName = btn.getAttribute("data-product-name");
            const quantity = btn.getAttribute("data-quantity");
            const deleteUrl = btn.getAttribute("data-delete");

            viewId.textContent = id;
            viewProductName.textContent = productName;
            viewQuantity.textContent = quantity;
            btnViewDelete.href = deleteUrl;

            btnViewEdit.onclick = () => {
                closeViewModal();
                formId.value = (id && id !== "undefined") ? id : "";
                formProduct.value = productId;
                formQuantity.value = quantity;

                modalTitle.textContent = "Editar Estoque";
                editModalOverlay.style.display = "flex";
            };

            viewModal.style.display = "flex";
        });
    });
});