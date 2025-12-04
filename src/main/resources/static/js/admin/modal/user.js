document.addEventListener("DOMContentLoaded", () => {
    const viewModal = document.getElementById("viewModalOverlay");
    const editModalOverlay = document.getElementById("modalOverlay");
    const openModalBtn = document.getElementById("openModal");

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
    const formPassword = document.getElementById("password");
    const modalTitle = document.getElementById("modalTitle");

    const closeViewModal = () => { if (viewModal) viewModal.style.display = "none"; };

    if (openModalBtn) {
        openModalBtn.addEventListener("click", () => {
            if (formId) formId.value = "";
            if (formName) formName.value = "";
            if (formEmail) formEmail.value = "";
            if (formRole) formRole.value = "CLIENTE";
            if (formPassword) {
                formPassword.value = "";
                formPassword.required = true;
            }
            if (modalTitle) modalTitle.textContent = "Adicionar Usuário";
            if (editModalOverlay) editModalOverlay.style.display = "flex";
        });
    }

    document.querySelectorAll(".edit").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            if (formId) formId.value = btn.getAttribute("data-id");
            if (formName) formName.value = btn.getAttribute("data-name");
            if (formEmail) formEmail.value = btn.getAttribute("data-email");
            if (formRole) formRole.value = btn.getAttribute("data-role");
            if (formPassword) {
                formPassword.value = "";
                formPassword.required = false;
            }
            if (modalTitle) modalTitle.textContent = "Editar Usuário";
            if (editModalOverlay) editModalOverlay.style.display = "flex";
        });
    });

    document.querySelectorAll(".view").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            if (viewId) viewId.textContent = btn.getAttribute("data-id");
            if (viewName) viewName.textContent = btn.getAttribute("data-name");
            if (viewEmail) viewEmail.textContent = btn.getAttribute("data-email");
            if (viewRole) viewRole.textContent = btn.getAttribute("data-role");
            if (btnViewDelete) btnViewDelete.href = btn.getAttribute("data-delete");

            if (btnViewEdit) {
                btnViewEdit.onclick = () => {
                    closeViewModal();
                    const originalEdit = document.querySelector(`.edit[data-id='${btn.getAttribute("data-id")}']`);
                    if (originalEdit) originalEdit.click();
                };
            }
            if (viewModal) viewModal.style.display = "flex";
        });
    });
});