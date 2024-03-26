import React from "react";
import button from "../../assets/lets discuss button.png";
import girl from "../../assets/image (1).png";
import "./HeroSection.css";

const HeroSection = () => {
  return (
    <div>
      <div className="hero">
        <div className="sub">
          <div className="hiDiv">
            <p className="hi">Hi, there!</p>
            <h1 className="luxeAssistance">
              <span className="lux">LUXE</span> IS HERE TO BE YOUR ASSISTANCE
            </h1>
            <p>
              I am here ready to help you in making creative digital products
            </p>
          </div>
          <div className="button">
            <img src={button} alt="button" />
          </div>
        </div>
        <div className="girlImage">
          <img src={girl} alt="girl" />
        </div>
      </div>
    </div>
  );
};

export default HeroSection;
