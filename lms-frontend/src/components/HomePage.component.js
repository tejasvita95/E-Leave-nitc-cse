import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import Slider from "react-slick";

export default class SimpleSlider extends Component {
  render() {
    const settings = {
      dots: true,
      infinite: true,
      // speed: 500,
      slidesToShow: 1,
      slidesToScroll: 1,
      autoplay: true,
      arrows: true,
      autoplaySpeed: 3000
    };
    return (
      <div>
 <center>
  
      
          <img
            src={require('.././images/NITC.png')}
            alt="no data found"
            height="90"
          />

<h1 style={{ display: 'inline',textAlign:'right', color:'white', fontSize:40}}>   &ensp; &ensp; Leave Management System  </h1>
          <Slider {...settings}>
            <div>
              <img
                src={require('.././images/1.png')}
                alt="no data found"
                height='424'
                width='800'

              />
            </div>
            <div>
              <img
                src={require('.././images/2.png')}
                alt="no data found"
                height='424'
                width='800'

              />

            </div>
            <div>
              <img
                src={require('.././images/3.jpg')}
                alt="no data found"
                height='424'
                width='800'

              />
            </div>
            <div>
              <img
                src={require('.././images/4.jpg')}
                alt="no data found"
                height='424'
                width='800'

              />
            </div>
            <div>
              <img
                src={require('.././images/5.png')}
                alt="no data found"
                height='424'
                width='800'
              />
            </div>
            <div>
              <img
                src={require('.././images/6.png')}
                alt="no data found"
                height='424'
                width='800'
              />
            </div>
          </Slider>
          </center>
          <br></br>
          <h4 style={{textAlign:'center', color:'white'}}> Department of Computer Science and Engineering </h4>
<h5 style={{ textAlign:'center', color:'white'}}> National Institute of Technology Calicut</h5>
<h5 style={{ textAlign:'center', color:'white'}}> NIT Campus (P.O), Kozhikode - 673601, Kerala</h5>

        </div>
    );
  }
}