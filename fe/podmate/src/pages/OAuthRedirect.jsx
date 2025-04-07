import { useEffect } from "react";

const OAuthRedirect = () => {
    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const status = urlParams.get("status");

        if (status === "success") {
            alert("로그인 성공!");
            window.location.href = "/";
        } else {
            alert("로그인 실패...");
        }
    }, []);

    return <div>로그인 처리 중...</div>;
};

export default OAuthRedirect;