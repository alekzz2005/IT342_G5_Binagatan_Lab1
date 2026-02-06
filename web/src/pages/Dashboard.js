import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { clearAuthToken, fetchMe, logoutUser } from "../services/api";

function Dashboard() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadUser = async () => {
      setError("");
      try {
        const data = await fetchMe();
        setUser(data);
      } catch (err) {
        setError("Session expired. Please log in again.");
        clearAuthToken();
        navigate("/");
      } finally {
        setLoading(false);
      }
    };
    loadUser();
  }, [navigate]);

  const handleLogout = async () => {
    try {
      await logoutUser();
    } catch (err) {
      // ignore
    } finally {
      clearAuthToken();
      navigate("/");
    }
  };

  if (loading) {
    return (
      <div className="auth-container">
        <div className="auth-card">
          <h2>Loading...</h2>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="dashboard-header">
          <h2>Dashboard</h2>
          <button className="ghost" onClick={handleLogout}>
            Logout
          </button>
        </div>
        {error && <div className="alert error">{error}</div>}
        {user && (
          <div className="profile">
            <div>
              <span>Username</span>
              <strong>{user.username}</strong>
            </div>
            <div>
              <span>Email</span>
              <strong>{user.email}</strong>
            </div>
            <div>
              <span>Member Since</span>
              <strong>
                {user.createdAt
                  ? new Date(user.createdAt).toLocaleString()
                  : "-"}
              </strong>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Dashboard;
