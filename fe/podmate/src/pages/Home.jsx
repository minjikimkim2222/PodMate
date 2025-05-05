const Home = () => {
    const handleLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/kakao";

    };

    return (
        <div>
            <h1>홈페이지</h1>
            <button onClick={handleLogin}>KaKao 로그인</button>
        </div>
    );
};

export default Home;