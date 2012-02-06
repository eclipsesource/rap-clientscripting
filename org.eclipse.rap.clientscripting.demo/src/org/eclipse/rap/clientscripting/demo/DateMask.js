var isNumber = function( character ) {
  var charCode = character.charCodeAt( 0 );
  return charCode >=48 && charCode <= 57;
};
    

var handleEvent = function( event ) { 

  if( event.type === SWT.KeyDown ) {
    var newCh = event.character;
    var keyCode = event.keyCode;
    var sel = event.widget.getSelection()[ 0 ];
    var text = event.widget.getText();
  
    var replaceNextChar = function( value ) {
      var leftPart = text.slice( 0, sel );
      var rightPart = text.slice( sel + 1, text.length );
      text = leftPart + value + rightPart;
      sel++;
    };
  
    var replacePrevChar = function( value ) {
      var leftPart = text.slice( 0, sel - 1 );
      var rightPart = text.slice( sel, text.length );
      text = leftPart + value + rightPart;
      sel--;
    };
    
    if( text[ sel ] === "_" && isNumber( newCh ) ) {
      replaceNextChar( newCh );
      if( text[ sel ] === "." ) {
        sel++;
      }    
    } else if( keyCode === SWT.BS && sel > 0 ) {
      if( text[ sel - 1 ] === "." ) {
        sel--;
      }
      if( isNumber( text[ sel - 1 ] ) ) {
        replacePrevChar( "_" );
      } else {
        sel--;
      }
      event.doit = false;
    }
  
    // TODO: Setting text also sets selection to last position - compare to SWT
    event.widget.setText( text );
    event.widget.setSelection( [ sel, sel ] );
  } 
  event.doit = false; // prevent key input and mousedown for selection change
};
