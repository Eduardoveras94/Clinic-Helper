<!-- top navigation -->
<div class="top_nav">
    <div class="nav_menu">
        <nav>
            <div class="nav toggle">
                <a id="menu_toggle"><i class="fa fa-bars"></i></a>
            </div>

            <ul class="nav navbar-nav navbar-right">
                <li class="">
                    <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        <img src="images/img.jpg" alt="">${user.getFullName()}
                        <span class=" fa fa-angle-down"></span>
                    </a>
                    <ul class="dropdown-menu dropdown-usermenu pull-right">
                        <li><a href="/user/${user.getUserId()}"><@spring.message "profile" /></a></li>
                        <li><a href="javascript:;"><@spring.message "help" /></a></li>
                        <li><a href="/logout"><i class="fa fa-sign-out pull-right"></i><@spring.message "logOut" /></a></li>
                    </ul>
                </li>

                <li role="presentation" class="dropdown">
                    <a href="javascript:;" class="dropdown-toggle info-number" data-toggle="dropdown" aria-expanded="false">
                        <i class="fa fa-bell-o"></i>
                        <span class="badge bg-green">${todoList?size}</span>
                    </a>
                    <#include "/layouts/_toDoList.ftl">
                </li>
            </ul>
        </nav>
    </div>
</div>
<!-- /top navigation -->