const modalConfirm = (title, body, option) => {
    const modalElement = document.getElementById("modal-confirm");
    modalElement.addEventListener("show.bs.modal", function (event) {
        let modalTitle = modalElement.querySelector(".modal-title");
        modalTitle.innerHTML = title;

        let modalBody = modalElement.querySelector(".modal-body");
        modalBody.innerHTML = body;
    });

    const modal = new bootstrap.Modal(modalElement);
    modal.show();
    if (typeof option.callback === "function") {
        const btnOk = modalElement.querySelector("#btn-ok");
        btnOk.addEventListener("click", option.callback);
        modalElement.addEventListener("hidden.bs.modal", function (event) {
            btnOk.removeEventListener("click", option.callback);
        });
    }
};

const prepareDeleteBoard = (id) => {
    const body = `<p class="text-center m-0">정말로 삭제하시겠습니까?</p>`;
    const deleteBoard = () => {
        const form = document.querySelector("#delete-board");
        form.action = `/boards/${id}`;
        form.submit();
    };
    modalConfirm("게시글 삭제", body, { callback: deleteBoard });
};

let quill;
const initEditor = () => {
    if (document.querySelector("#quill-editor")) {
        quill = new Quill("#quill-editor", {
            modules: {
                imageResize: { displaySize: true },
                toolbar: [
                    [{ header: [1, 2, 3, 4, 5, 6, false] }],
                    ["bold", "italic", "underline", "strike"],
                    ["image", "link"],
                    [{ script: "sub" }, { script: "super" }],
                    [{ list: "ordered" }, { list: "bullet" }],
                    ["clean"],
                ],
            },
            theme: "snow",
        });
        quill.on("text-change", function (delta, source) {
            updateHtmlOutput();
        });
    }
};
const getQuillHtml = () => quill.root.innerHTML;
const updateHtmlOutput = () => {
    let html = getQuillHtml();
    document.getElementById("content").innerHTML = html;
};

const handleCheckSecret = (evt) => {
    document.querySelector(".checkSecret-related").disabled = !evt.currentTarget.checked;
    if (!evt.currentTarget.checked) document.querySelector(".checkSecret-related").value = "";
};
const listenerCheckSecret = () => {
    const element = document.getElementById("checkSecret");
    if (element) {
        element.addEventListener("change", handleCheckSecret);
        document.querySelector(".checkSecret-related").disabled = !element.checked;
    }
};

const prepareShowBoard = (id, needConfirm) => {
    console.log("prepareShowBoard", id, needConfirm);
    const retPage = location.href;
    if (!needConfirm) location.href = `/boards/${id}?return_page=${retPage}`;
    else {
        const body = `
            <form class="px-5" id="frm-nav-board" action="/boards/${id}" method="get">
            <label for="password" class="form-label">비밀번호를 입력하세요.</label>
                <input type="hidden" name="return_page" value="${retPage}" />
                <input type="text" class="form-control" name="password" />
            </form>
        `;
        const navigationBoard = () => {
            const form = document.querySelector("#frm-nav-board");
            form.submit();
        };
        modalConfirm("비밀글 보기", body, { callback: navigationBoard });
    }
};


const prepareEditBoard = (id, needConfirm) => {
    console.log("prepareEditBoard", id, needConfirm);
    const retPage = location.href;
    if (!needConfirm) location.href = `/boards/${id}/edit?return_page=${retPage}`;
    else {
        const body = `
            <form class="px-5" id="frm-nav-board" action="/boards/${id}/edit" method="get">
            <label for="password" class="form-label">비밀번호를 입력하세요.</label>
                <input type="hidden" name="return_page" value="${retPage}" />
                <input type="text" class="form-control" name="password" />
            </form>
        `;
        const navigationBoard = () => {
            const form = document.querySelector("#frm-nav-board");
            form.submit();
        };
        modalConfirm("비밀글 수정하기", body, { callback: navigationBoard });
    }
};

window.addEventListener("load", () => {
    initEditor();
    listenerCheckSecret();
});
