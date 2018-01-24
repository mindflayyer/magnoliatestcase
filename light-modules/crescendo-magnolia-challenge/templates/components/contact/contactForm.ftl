<form method="post">
    
    <div>
        <label>First Name:</label>
        <input type="text" name="firstName" value="${model.firstName}"/>
    <#if !model.firstNameValid><span style="color:#ff6249">*</span></#if>
    </div>
    <div>
        <label>Last Name:</label>
        <input type="text" name="lastName" value="${model.lastName}"/>
        <#if !model.lastNameValid><span style="color:#ff6249">*</span></#if>
    </div>
    <div>
        <label>E-mail:</label>
        <input type="text" name="email" value="${model.email}"/>
        <#if !model.emailValid><span style="color:#ff6249">*</span></#if>
    </div>
    <div>
        <input type="checkbox" name="join" ${model.addToMailingList?string('checked', '')} />
        <label>Join the mailing list</label>
    </div>
    <div>
        <input type="submit" value="Submit Form" onclick="${model.sendMail(content.recipient!,
        content.username!, content.password!, content.smtpServer!, content.smtpPort!,
        content.smtpAuth!, content.smtpTlsEnabled!)}"/>
    </div>
    <div>
         <#if model.statusMessage??>${model.statusMessage}<#else> </#if>
    </div>
</form>