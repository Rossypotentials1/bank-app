import React from 'react'
import luxe from "../../assets/Luxe.png";
import navBar from "../../assets/Menubar.png";
import "./index.css"

const Header = () => {
  return (
    <nav className='nav'>
        <div className='logo'>
            <img src={luxe} alt='Logo' />
        </div>
        <div className='menuDiv'>
            <ul className='menuUnorderList'>
                <li>Home</li>
                <li>Service</li>
                <li>Portfolio</li>
                <li>How we Work</li>
            </ul>
        </div>
        <div className='navIcon'><img src={navBar} alt='NavBar' /></div>
    </nav>
  )
}

export default Header;
