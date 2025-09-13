import React, { useState, useRef } from 'react';
import Speedometer from './Speedometer';

const NavigateMeComponent = (props) => {
  return (
   <div style={{display:'flex'}}>
    <img src='/maps.jpg'  style={{ maxWidth: '200px', height:'400px', marginTop: '1rem' }}/>
    <Speedometer/>
   </div>
  );
};

export default NavigateMeComponent;
