var handleEvent = function( event ) {
  
  if( event.keyCode === 0 && event.text.length > 1 ) {
    event.text = "dont paste here!";
  } else {
    event.text = event.text.toUpperCase();
  }

};
