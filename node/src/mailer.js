nodemailer = require("nodemailer");

// create reusable transport method (opens pool of SMTP connections)
var smtpTransport = nodemailer.createTransport("SMTP",{
    service: "Gmail",
    auth: {
        user: "testmail.magnet@gmail.com",
        pass: "rS5AxI6zYGpP"
    }
});

User = require("./mongo").User

user_emails = []

User.find().exec(function(err,users) {
  for(var i=0; i<users.length; ++i) {
      user_emails.push(users[i].email);
  }
  console.log(user_emails);

  // setup e-mail data with unicode symbols
  var mailOptions = {
    from: "Test Mailer [0m~\~T <testmail.magnet@gmail.com>", // sender address
    to: user_emails.join(","), // list of receivers
    subject: "Hello [0m~\~T", // Subject line
    text: "Hello world [0m~\~T", // plaintext body
    html: "<b>Hello world [0m~\~T</b>" // html body
  }

  // send mail with defined transport object
  smtpTransport.sendMail(mailOptions, function(error, response){
      if(error){
          console.log(error);
      }else{
          console.log("Message sent: " + response.message);
      }

      // if you don't want to use this transport object anymore, uncomment following line
      //smtpTransport.close(); // shut down the connection pool, no more messages
  });

  process.exit(code=0)

});

