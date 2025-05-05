import { BrowserRouter, Routes, Route } from "react-router-dom";
import OAuthRedirect from "./pages/OAuthRedirect";
import Home from "./pages/Home";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/oauth/redirect" element={<OAuthRedirect />} />
                <Route path="/" element={<Home />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
