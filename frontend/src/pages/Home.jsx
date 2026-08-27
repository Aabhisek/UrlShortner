import { Link } from "react-router-dom";

function Home() {
    return (
        <div>
            <h1>URL Shortener</h1>

            <p>
                Shorten your long URLs and share them easily.
            </p>

            <Link to="/login">
                <button>Login</button>
            </Link>

            <Link to="/register">
                <button>Register</button>
            </Link>
        </div>
    );
}

export default Home;