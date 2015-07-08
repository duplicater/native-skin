'use strict';

var React = require('react-native');
var {
  Image,
  ListView,
  StyleSheet,
  Text,
  TouchableHighlight,
  View,
} = React;

var indexIos = require('./index.ios.js');
var Utils = require('./utils');
var Constants = require('./constants');
var RectButton = require('./widgets/RectButton');
var styles = Utils.getStyles(require('./style/moreOptionScreenStyles.json'));
var config = require('./skin-config/skin.json');

var {
  ICONS,
  BUTTON_NAMES,
  IMG_URLS,
} = Constants;

var moreOptionButtonSize = 30;
var dismissButtonSize = 20;

var MoreOptionScreen = React.createClass({
	propTypes: {
    onPress: React.PropTypes.func,
    onDismiss: React.PropTypes.func,
    onSocialButtonPress: React.PropTypes.func,
    sharePanel: React.PropTypes.object,
    buttonSelected: React.PropTypes.string,
    panelToShow: React.PropTypes.string,
    onOptionButtonPress: React.PropTypes.func,
	},

  _renderButton: function(style, icon, func, size) {
    return (
      <RectButton
        icon={icon}
        onPress={func}
        fontSize={size}
        style={style}>
      </RectButton>
    );
  },

  _renderMoreOptionButtons: function(moreOptionButtons){
    for(var i = 0; i < config.buttons.length; i++){
      var button = config.buttons[i];

      if(button.type == "FeatureOptions" || button.type == "MoreOptions"){
        var moreOptionButton;
        var buttonStyle;
        var buttonIcon;

        if(this.props.buttonSelected == "None"|| this.props.buttonSelected == button.name){
          buttonStyle = styles.iconBright;
        }else{
          buttonStyle = styles.iconDark;
        }
        
        switch(button.name){
          case "Discovery":
            buttonIcon = ICONS.DISCOVERY;
            break;
          case "Quality":
            buttonIcon = ICONS.QUALITY;
            break;
          case "CC":
            buttonIcon = ICONS.CC;
            break;
          case "Share":
            buttonIcon = ICONS.SHARE;
            break;
          case "Setting":
            buttonIcon = ICONS.SETTING;
            break;
          default:
            break;
        }

        var onOptionPress = function(buttonName, f){
          return function(){
            f(buttonName);
          };
        }(button.name, this.props.onOptionButtonPress);

        moreOptionButton = this._renderButton(buttonStyle, buttonIcon, onOptionPress, moreOptionButtonSize);

        moreOptionButtons.push(moreOptionButton);
      }
    }
  },

	render: function() {
    var moreOptionButtons = [];
    this._renderMoreOptionButtons(moreOptionButtons);
    
    var dismissButton = this._renderButton(styles.iconBright, ICONS.DISMISS, this.props.onDismiss, dismissButtonSize);

    var moreOptionRow = (
      <View
        ref='moreOptionRow' 
        style={this.props.buttonSelected != "None"? styles.rowBottom: styles.rowCenter}>
        {moreOptionButtons}
      </View>
    );
    
    var dismissButtonRow = (
      <View style={styles.dismissButtonTopRight}>
        {dismissButton}
      </View>
    );


    var sharePanel;
    if(this.props.panelToShow == "Share"){
      console.log("i am into it, ");
      sharePanel = this.props.sharePanel;
    }

    var moreOptionScreen = (
      <View style={styles.fullscreenContainer}>
        {sharePanel}
        {dismissButtonRow}
        {moreOptionRow}
      </View>
    );

    return (
      <View style={styles.fullscreenContainer}>
        {moreOptionScreen}
      </View>
    );
  }
});

module.exports = MoreOptionScreen;