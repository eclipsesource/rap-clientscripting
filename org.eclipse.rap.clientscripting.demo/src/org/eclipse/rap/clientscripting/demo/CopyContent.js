function handleEvent( event ) {
  if( changeFlag == 0 ) { // prevent recursive behavior, only required in SWT (currently)
    changeFlag = 1;// boolean not yet supported by context creator
    var value = event.widget.getText();
    // prevent the selection from being reset, only really required in SWT (currently)
    if( value != text1.getText() ) {
      text1.setText( value );
    }
    if( value != text2.getText() ) {
      text2.setText( value );
    }
    changeFlag = 0;
  }
};