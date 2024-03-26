import React from 'react'
import brand from "../../assets/Vector1.png";
import uiux from "../../assets/Ui DesignSymbol.png";
import productdesign from "../../assets/ProductSymbol.png";
import './index.css';

const Product = () => {
  return (
    <div className='product'>
     <h2 className='service'>OUR SERVICE</h2>
     <div className='productContain'>
     <div className='brand'>
     <div className='brandImageDiv'><img src={brand} alt='brandImage' /></div>
     <div className='brandLoremDiv'>
      <h5 className='brandingHeader'>Branding</h5>
      <p className='lorem'> Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
     </div>
     </div>
     <div className='uiux'>
     <div className='uiuxImageDiv'><img src={uiux} alt='brandImage' /></div>
     <div className='uiuxLoremDiv'>
      <h5 className='uiHeader'>UI/UX</h5>
      <p className='lorem'>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
     </div>
     </div>

     <div className='productDesignDiv'>
     <div className='productDesignImageDiv'><img src={productdesign} alt='brandImage' /></div>
     <div className='productLorem'>
      <h5 className='productHeader'>Product Design</h5>
      <p className='lorem'>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
     </div>
     </div>
     </div>
    </div>
  )
}

export default Product
