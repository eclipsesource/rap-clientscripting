function handleEvent( event ) {
  if( this.changeFlag == 0 ) { // prevent recursive behavior, only required in SWT (currently)
    this.changeFlag = 1;// boolean not yet supported by context creator
    var value = event.widget.getText();
    // prevent the selection from being reset, only really required in SWT (currently)
    if( value != this.text1.getText() ) {
      this.text1.setText( value );
    }
    if( value != this.text2.getText() ) {
      this.text2.setText( value );
    }
    this.changeFlag = 0;
  }
};