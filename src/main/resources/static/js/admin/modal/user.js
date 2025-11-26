document.addEventListener("DOMContentLoaded", () => {
    const viewModal = document.getElementById("viewModalOverlay");
    const editModalOverlay = document.getElementById("modalOverlay");

    const viewId = document.getElementById("viewId");
    const viewName = document.getElementById("viewName");
    const viewEmail = document.getElementById("viewEmail");
    const viewRole = document.getElementById("viewRole");
    const btnViewEdit = document.getElementById("btnViewEdit");
    const btnViewDelete = document.getElementById("btnViewDelete");

    const formId = document.getElementById("userId");
    const formName = document.getElementById("name");
    const formEmail = document.getElementById("email");
    const formRole = document.getElementById("role");
    const modalTitle = document.getElementById("modalTitle");
    const openModalBtn = document.getElementById("openModal");

    const closeViewModal = () => viewModal.style.display = "none";

    if (openModalBtn) {
        openModalBtn.addEventListener("click", () => {
            formId.value = "";
            formName.value = "";
            formEmail.value = "";
            formRole.value = "CLIENTE";
            modalTitle.textContent = "Adicionar Usuário";
            editModalOverlay.style.display = "flex";
        });
    }

    document.querySelectorAll(".edit").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            const id = btn.getAttribute("data-id");

            formId.value = id || "";
            formName.value = btn.getAttribute("data-name");
            formEmail.value = btn.getAttribute("data-email");
            formRole.value = btn.getAttribute("data-role");

            modalTitle.textContent = "Editar Usuário";
            editModalOverlay.style.display = "flex";
        });
    });

    document.querySelectorAll(".view").forEach(btn => {
        btn.addEventListener("click", (event) => {
            event.preventDefault();

            const id = btn.getAttribute("data-id");
            const name = btn.getAttribute("data-name");
            const email = btn.getAttribute("data-email");
            const role = btn.getAttribute("data-role");
            const deleteUrl = btn.getAttribute("data-delete");

            viewId.textContent = id;
            viewName.textContent = name;
            viewEmail.textContent = email;
            viewRole.textContent = role;
            btnViewDelete.href = deleteUrl;

            btnViewEdit.onclick = () => {
                closeViewModal();
                formId.value = (id && id !== "undefined") ? id : "";
                formName.value = name;
                formEmail.value = email;
                formRole.value = role;

                modalTitle.textContent = "Editar Usuário";
                editModalOverlay.style.display = "flex";
            };

            viewModal.style.display = "flex";
        });
    });
});