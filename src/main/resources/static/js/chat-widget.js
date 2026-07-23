document.addEventListener("DOMContentLoaded", () => {
    const widget = document.getElementById("ai-chat-widget");

    if (!widget) {
        return;
    }

    const endpoint = widget.dataset.endpoint || "/api/chat";
    const toggleButton = document.getElementById("chat-toggle");
    const closeButton = document.getElementById("chat-close");
    const panel = document.getElementById("chat-panel");
    const input = document.getElementById("chat-input");
    const sendButton = document.getElementById("chat-send");
    const messages = document.getElementById("chat-messages");
    const status = document.getElementById("chat-status");

    if (
        !toggleButton ||
        !closeButton ||
        !panel ||
        !input ||
        !sendButton ||
        !messages ||
        !status
    ) {
        console.error("AI Chatbox: thiếu thành phần HTML cần thiết.");
        return;
    }

    toggleButton.addEventListener("click", () => {
        const currentlyOpen = panel.style.display === "flex";

        if (currentlyOpen) {
            closeChat();
        } else {
            openChat();
        }
    });

    closeButton.addEventListener("click", closeChat);
    sendButton.addEventListener("click", sendMessage);

    input.addEventListener("keydown", event => {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            sendMessage();
        }
    });

    function openChat() {
        panel.style.display = "flex";
        panel.style.flexDirection = "column";
        panel.setAttribute("aria-hidden", "false");
        toggleButton.setAttribute("aria-expanded", "true");

        input.focus();
        scrollToBottom();
    }

    function closeChat() {
        panel.style.display = "none";
        panel.setAttribute("aria-hidden", "true");
        toggleButton.setAttribute("aria-expanded", "false");
    }

    async function sendMessage() {
        const message = input.value.trim();

        if (!message) {
            status.textContent = "Vui lòng nhập câu hỏi.";
            input.focus();
            return;
        }

        if (message.length > 500) {
            status.textContent = "Tin nhắn không được vượt quá 500 ký tự.";
            return;
        }

        appendMessage(message, "user");
        input.value = "";
        setLoading(true);

        const loadingElement = appendLoadingMessage();

        try {
            const headers = {
                "Content-Type": "application/json"
            };

            const csrfToken = document
                .querySelector('meta[name="_csrf"]')
                ?.getAttribute("content");

            const csrfHeader = document
                .querySelector('meta[name="_csrf_header"]')
                ?.getAttribute("content");

            if (csrfToken && csrfHeader) {
                headers[csrfHeader] = csrfToken;
            }

            const response = await fetch(endpoint, {
                method: "POST",
                headers: headers,
                body: JSON.stringify({
                    message: message
                })
            });

            let responseBody = null;

            try {
                responseBody = await response.json();
            } catch (parseError) {
                console.error(
                    "AI Chatbox: response không phải JSON hợp lệ.",
                    parseError
                );
            }

            if (!response.ok) {
                const errorMessage =
                    responseBody?.message ||
                    responseBody?.detail ||
                    "Không thể gửi câu hỏi tới AI.";

                throw new Error(errorMessage);
            }

            const answer = responseBody?.answer;

            if (!answer) {
                throw new Error("AI không trả về câu trả lời.");
            }

            appendMessage(answer, "assistant");
            status.textContent = "";
        } catch (error) {
            console.error("AI Chatbox error:", error);

            appendMessage(
                "Dịch vụ AI hiện không khả dụng. Vui lòng thử lại sau.",
                "error"
            );

            status.textContent = error.message;
        } finally {
            loadingElement.remove();
            setLoading(false);
            input.focus();
        }
    }

    function appendMessage(text, type) {
        const wrapper = document.createElement("div");
        const bubble = document.createElement("span");

        wrapper.classList.add("mb-3");
        bubble.classList.add(
            "d-inline-block",
            "rounded-3",
            "p-2",
            "text-break"
        );

        if (type === "user") {
            wrapper.classList.add("text-end");
            bubble.classList.add("bg-danger", "text-white");
        } else if (type === "error") {
            wrapper.classList.add("text-start");
            bubble.classList.add("bg-warning", "text-dark", "border");
        } else {
            wrapper.classList.add("text-start");
            bubble.classList.add("bg-white", "border");
        }

        bubble.textContent = text;

        wrapper.appendChild(bubble);
        messages.appendChild(wrapper);
        scrollToBottom();
    }

    function appendLoadingMessage() {
        const wrapper = document.createElement("div");
        const bubble = document.createElement("span");

        wrapper.className = "mb-3 text-start";
        bubble.className =
            "d-inline-block bg-white border rounded-3 p-2 text-muted";
        bubble.textContent = "AI đang trả lời...";

        wrapper.appendChild(bubble);
        messages.appendChild(wrapper);
        scrollToBottom();

        return wrapper;
    }

    function setLoading(loading) {
        input.disabled = loading;
        sendButton.disabled = loading;
        sendButton.textContent = loading ? "Đang gửi..." : "Gửi";
        status.textContent = loading
            ? "Đang kết nối tới trợ lý AI..."
            : "";
    }

    function scrollToBottom() {
        messages.scrollTop = messages.scrollHeight;
    }
});
