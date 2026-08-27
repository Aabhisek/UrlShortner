import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function Login() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: "",
        password: ""
    });

    const [error, setError] = useState("");

    const handleChange = (event) => {
        setFormData({
            ...formData,
            [event.target.name]: event.target.value
        });
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");

        try {

            const response = await api.post(
                "/auth/login",
                formData
            );

            const token = response.data.token;

            localStorage.setItem("token", token);

            navigate("/dashboard");

        } catch (error) {

            if (error.response) {
                setError(
                    error.response.data?.message ||
                    "Invalid username or password"
                );
            } else {
                setError("Unable to connect to server");
            }
        }
    };

    return (
        <div>

            <h1>Login</h1>

            <form onSubmit={handleSubmit}>

                <div>
                    <label>Username</label>

                    <input
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div>
                    <label>Password</label>

                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button type="submit">
                    Login
                </button>

            </form>

            {error && (
                <p>{error}</p>
            )}

            <button onClick={() => navigate("/register")}>
                Create an account
            </button>

        </div>
    );
}

export default Login;