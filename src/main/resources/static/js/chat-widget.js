document.addEventListener("DOMContentLoaded", () => {
    const widget = document.getElementById("ai-chat-widget");

    if (!widget) {
        return;
    }

    const endpoint = widget.dataset.endpoint || "/api/chat";
    const statusEndpoint = widget.dataset.statusEndpoint || "/api/chat/status";
    const requestTimeoutMs = 25_000;
    const toggleButton = document.getElementById("chat-toggle");
    const closeButton = document.getElementById("chat-close");
    const clearButton = document.getElementById("chat-clear");
    const panel = document.getElementById("chat-panel");
    const input = document.getElementById("chat-input");
    const sendButton = document.getElementById("chat-send");
    const messages = document.getElementById("chat-messages");
    const status = document.getElementById("chat-status");
    const availabilityBadge = document.getElementById("chat-availability");
    const suggestionButtons = widget.querySelectorAll(
        "[data-chat-suggestion]"
    );

    if (
        !toggleButton ||
        !closeButton ||
        !clearButton ||
        !panel ||
        !input ||
        !sendButton ||
        !messages ||
        !status ||
        !availabilityBadge
    ) {
        console.error("AI Chatbox: thiếu thành phần HTML cần thiết.");
        return;
    }
    const initialMessages = messages.innerHTML;
    let aiAvailable = false;
    let availabilityMessage = "Đang kiểm tra cấu hình AI...";

    setAiAvailability(false, availabilityMessage);

    toggleButton.addEventListener("click", () => {
        const currentlyOpen = panel.style.display === "flex";

        if (currentlyOpen) {
            closeChat();
        } else {
            openChat();
        }
    });

    closeButton.addEventListener("click", closeChat);
    clearButton.addEventListener("click", clearConversation);
    sendButton.addEventListener("click", () => sendMessage());
    suggestionButtons.forEach(button => {
        button.addEventListener("click", () => {
            const suggestion = button.dataset.chatSuggestion?.trim();

            if (!suggestion || sendButton.disabled) {
                return;
            }

            input.value = suggestion;
            sendMessage();
        });
    });


    input.addEventListener("keydown", event => {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            sendMessage();
        }
    });
    document.addEventListener("keydown", event => {
        if (event.key === "Escape" && panel.style.display === "flex") {
            closeChat();
        }
    });

    loadAiStatus();


    async function loadAiStatus() {
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 5_000);

        try {
            const response = await fetch(statusEndpoint, {
                headers: {
                    "Accept": "application/json"
                },
                signal: controller.signal
            });
            const responseBody = await response.json();

            if (!response.ok) {
                throw new Error("Không thể kiểm tra trạng thái AI.");
            }

            setAiAvailability(
                Boolean(responseBody?.available),
                responseBody?.message || "Không thể xác định trạng thái AI."
            );
        } catch (error) {
            const errorMessage = error.name === "AbortError"
                ? "Kiểm tra trạng thái AI mất quá nhiều thời gian."
                : "Không thể kiểm tra trạng thái AI. Vui lòng thử lại sau.";

            console.error("AI Chatbox status error:", error);
            setAiAvailability(false, errorMessage);
        } finally {
            clearTimeout(timeoutId);
        }
    }

    function setAiAvailability(available, message) {
        aiAvailable = available;
        availabilityMessage = message;
        availabilityBadge.hidden = available;
        input.disabled = !available;
        sendButton.disabled = !available;
        suggestionButtons.forEach(button => {
            button.disabled = !available;
        });
        status.textContent = "";

        messages.innerHTML = initialMessages;

        if (!available) {
            appendMessage(availabilityMessage, "error");
        }
    }
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
        toggleButton.focus();
    }

    async function sendMessage(messageOverride = null) {
        if (sendButton.disabled || !aiAvailable) {
            return;
        }

        const message = (messageOverride ?? input.value).trim();

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

        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), requestTimeoutMs);
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
                }),
                signal: controller.signal
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
                    responseBody?.error ||
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
            const errorMessage = error.name === "AbortError"
                ? "Yêu cầu đã quá 25 giây. Vui lòng thử lại."
                : error instanceof TypeError
                    ? "Không thể kết nối tới AI. Vui lòng thử lại."
                    : error.message ||
                        "Dịch vụ AI hiện không khả dụng. Vui lòng thử lại sau.";

            console.error("AI Chatbox error:", error);
            appendErrorMessage(errorMessage, message);
            status.textContent = errorMessage;
        } finally {
            clearTimeout(timeoutId);
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
        bubble.style.whiteSpace = "pre-wrap";

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
    function appendErrorMessage(text, originalMessage) {
        const wrapper = document.createElement("div");
        const bubble = document.createElement("span");
        const retryButton = document.createElement("button");

        wrapper.className = "mb-3 text-start";
        bubble.className =
            "d-inline-block bg-warning text-dark border rounded-3 p-2 text-break";
        bubble.style.whiteSpace = "pre-wrap";
        bubble.textContent = text;

        retryButton.type = "button";
        retryButton.className = "btn btn-sm btn-outline-danger d-block mt-2";
        retryButton.dataset.chatRetry = "true";
        retryButton.textContent = "Thử lại";
        retryButton.addEventListener("click", () => {
            retryButton.remove();
            sendMessage(originalMessage);
        });

        wrapper.appendChild(bubble);
        wrapper.appendChild(retryButton);
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

    function clearConversation() {
        messages.innerHTML = initialMessages;
        input.value = "";
        status.textContent = "";

        if (!aiAvailable) {
            appendMessage(availabilityMessage, "error");
        }

        input.focus();
    }
    function setLoading(loading) {
        const controlsDisabled = loading || !aiAvailable;
        input.disabled = controlsDisabled;
        sendButton.disabled = controlsDisabled;
        clearButton.disabled = loading;
        suggestionButtons.forEach(button => {
            button.disabled = controlsDisabled;
        });
        messages.querySelectorAll("[data-chat-retry]").forEach(button => {
            button.disabled = controlsDisabled;
        });
        messages.setAttribute("aria-busy", String(loading));
        sendButton.textContent = loading ? "Đang gửi..." : "Gửi";

        if (loading) {
            status.textContent = "Đang kết nối tới trợ lý AI...";
        }
    }

    function scrollToBottom() {
        messages.scrollTop = messages.scrollHeight;
    }
});
