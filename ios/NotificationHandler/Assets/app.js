var apn = require('apn');

var apnProvider = new apn.Provider({
     token: {
        key: 'apns.p8',
        keyId: 'VQL5L4G6XQ',
        teamId: 'D5J4ZY4W89',
    },
    production: false // Set to true if sending a notification to a production iOS app
});

var deviceToken = process.argv[2];

var notification = new apn.Notification();

notification.topic = 'com.nwalker2398.VTAOperationManagersAssistant.VTA-Operations-Managers-Assistant';

notification.expiry = Math.floor(Date.now() / 1000) + 3600;

notification.badge = 3;

notification.sound = 'ping.aiff';

notification.alert = 'Hello World \u270C';

notification.payload = {id: 123};

apnProvider.send(notification, deviceToken).then(function(result) {
    console.log(result);
})
