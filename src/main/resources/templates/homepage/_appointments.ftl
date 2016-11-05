<div class="col-md-8">
    <h2>Today's appointments</h2>
    <div class="x_content">
        <div class="row">
        <#list todays_appointments as appointment>
            <div class="col-md-12 col-sm-12 col-xs-12 profile_details">
                <div class="well profile_view">
                    <div class="col-sm-12">
                        <h4 class="brief"><i>${appointment.getAppointmentType()}
                            AT ${appointment.getSimplifiedTime()} </i></h4>
                        <div class="left col-xs-7">
                            <h2>${appointment.patient.getPatientFullName()}</h2>
                            <p><strong>Description: </strong>${appointment.appointmentDescription} </p>
                            <ul class="list-unstyled">
                                <li><i class="fa fa-building"></i> Address:</li>
                                <li><i class="fa fa-phone"></i> Phone
                                    #:${appointment.patient.getPatientTelephoneNumber()}</li>
                            </ul>
                        </div>
                        <div class="right col-xs-5 text-center">
                            <img src="images/user.png" alt="" style="max-width: 55%;" class="img-circle img-responsive">
                        </div>
                    </div>
                    <div class="col-xs-12 bottom text-center">

                        <div class="col-xs-12 col-sm-6 emphasis">
                            <button type="button" class="btn btn-danger btn-sm btn-round"><i class="fa fa-user">
                            </i> DELETE </i></button>
                        </div>
                        <div class="col-xs-12 col-sm-6 emphasis">
                            <button data-toggle="dropdown" class="btn btn-default dropdown-toggle btn-sm btn-round" type="button" aria-expanded="false">Options <span class="caret"></span>
                            </button>
                            <ul role="menu" class="dropdown-menu">
                                <li><a href="#">Mark as HERE</a>
                                </li>
                                <li><a href="#">Mark as READY</a>
                                </li>
                                <li><a href="#">Something else here</a>
                                </li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a>
                                </li>
                            </ul>
                            
                            <button type="button" class="btn btn-primary btn-sm btn-round" >
                                <i class="fa fa-user"> </i> View Profile
                            </button>





                        </div>
                    </div>
                </div>
            </div>
        <#else>
            <p><h4>You have don't have more appointments to show</h4></p>
        </#list>
        </div>
    </div>
</div>