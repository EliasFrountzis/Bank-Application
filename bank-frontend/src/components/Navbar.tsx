import logo from "../assets/SPlogo.png";

interface NavbarProps {
    onLogout: () => void;
}

function Navbar({ onLogout }: NavbarProps) {
    return (
        <nav className="navbar">

            <div className="navbar-inner">

                <div className="navbar-brand">

                  <img
    className="logo"
    src={logo}
    alt="SpBank logo"
/>

                    <span>
                        SpBank
                    </span>

                </div>


                <div className="navbar-links">

                    <span className="nav-link active">
                        Dashboard
                    </span>

                    
                    <button
                        className="logout-button"
                        onClick={onLogout}
                    >
                        Logout
                    </button>

                </div>

            </div>

        </nav>
    );
}

export default Navbar;