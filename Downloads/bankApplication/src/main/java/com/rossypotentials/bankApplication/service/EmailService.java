package com.rossypotentials.bankApplication.service;

import com.rossypotentials.bankApplication.payload.request.EmailDetails;

public interface EmailService {
   void  sendEmailAlert (EmailDetails emailDetails);
   void  sendEmailWithAttachment (EmailDetails emailDetails);

}
