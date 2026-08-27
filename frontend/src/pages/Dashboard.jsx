import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function Dashboard() {

    const navigate = useNavigate();

    const [originalUrl, setOriginalUrl] = useState("");
    const [shortUrl, setShortUrl] = useState("");
    const [urls, setUrls] = useState([]);

    const [loading, setLoading] = useState(false);
    const [loadingUrls, setLoadingUrls] = useState(true);

    const [error, setError] = useState("");
    const [message, setMessage] = useState("");

    // Load user's URLs when dashboard opens
    useEffect(() => {
        fetchUrls();
    }, []);

    const fetchUrls = async () => {

        try {

            setLoadingUrls(true);
            setError("");

            const response = await api.get("/urls");

            setUrls(response.data);

        } catch (error) {

            if (
                error.response?.status === 401 ||
                error.response?.status === 403
            ) {
                localStorage.removeItem("token");
                navigate("/login");
                return;
            }

            setError("Unable to load your URLs");

        } finally {

            setLoadingUrls(false);
        }
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");
        setMessage("");
        setShortUrl("");
        setLoading(true);

        try {

            const response = await api.post("/urls", {
                originalUrl: originalUrl
            });

            const data = response.data;

            const completeShortUrl =
                `http://shrtly/${data.shortUrl}`;

            setShortUrl(completeShortUrl);
            setMessage("URL shortened successfully");

            setOriginalUrl("");

            // Add newly created URL to the table
            setUrls((previousUrls) => [
                data,
                ...previousUrls
            ]);

        } catch (error) {

            if (
                error.response?.status === 401 ||
                error.response?.status === 403
            ) {
                localStorage.removeItem("token");
                navigate("/login");
                return;
            }

            setError(
                error.response?.data?.message ||
                "Unable to create short URL"
            );

        } finally {

            setLoading(false);
        }
    };

    const handleCopy = async (url) => {

        try {

            await navigator.clipboard.writeText(url);

            setMessage("Short URL copied!");

        } catch {

            setError("Unable to copy URL");

        }
    };

    const handleLogout = () => {

        localStorage.removeItem("token");

        navigate("/login");
    };

    return (
        <div>

            <header>
                <h1>URL Shortener</h1>

                <button onClick={handleLogout}>
                    Logout
                </button>
            </header>

            <main>

                <h2>Dashboard</h2>

                <p>
                    Shorten your long URLs.
                </p>

                {/* Create URL */}

                <form onSubmit={handleSubmit}>

                    <input
                        type="url"
                        placeholder="Enter your long URL"
                        value={originalUrl}
                        onChange={(event) =>
                            setOriginalUrl(event.target.value)
                        }
                        required
                    />

                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Creating..."
                            : "Shorten URL"
                        }
                    </button>

                </form>

                {/* Messages */}

                {message && (
                    <p>{message}</p>
                )}

                {error && (
                    <p>{error}</p>
                )}

                {/* Newly created URL */}

                {shortUrl && (
                    <div>

                        <p>
                            Your new short URL:
                        </p>

                        <a
                            href={shortUrl}
                            target="_blank"
                            rel="noreferrer"
                        >
                            {shortUrl}
                        </a>

                        <button
                            onClick={() =>
                                handleCopy(shortUrl)
                            }
                        >
                            Copy
                        </button>

                    </div>
                )}

                {/* URL list */}

                <section>

                    <h2>Your URLs</h2>

                    {loadingUrls ? (
                        <p>Loading your URLs...</p>
                    ) : urls.length === 0 ? (
                        <p>
                            You haven't created any URLs yet.
                        </p>
                    ) : (

                        <table>

                            <thead>

                                <tr>
                                    <th>Original URL</th>
                                    <th>Short URL</th>
                                    <th>Clicks</th>
                                    <th>Created</th>
                                    <th>Action</th>
                                </tr>

                            </thead>

                            <tbody>

                                {urls.map((url) => {

                                    const completeShortUrl =
                                        `http://shrtly/${url.shortUrl}`;

                                    return (
                                        <tr key={url.id}>

                                            <td>
                                                {url.originalUrl}
                                            </td>

                                            <td>

                                                <a
                                                    href={completeShortUrl}
                                                    target="_blank"
                                                    rel="noreferrer"
                                                >
                                                    {completeShortUrl}
                                                </a>

                                            </td>

                                            <td>
                                                {url.clickCount}
                                            </td>

                                            <td>
                                                {new Date(
                                                    url.createdDate
                                                ).toLocaleString()}
                                            </td>

                                            <td>

                                                <button
                                                    onClick={() =>
                                                        handleCopy(
                                                            completeShortUrl
                                                        )
                                                    }
                                                >
                                                    Copy
                                                </button>

                                            </td>

                                        </tr>
                                    );
                                })}

                            </tbody>

                        </table>

                    )}

                </section>

            </main>

        </div>
    );
}

export default Dashboard;