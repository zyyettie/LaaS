LaaS = new Marionette.Application();

LaaS.addRegions({
    headerRegion: '#header',
    mainNavRegion: '#nav',
    mainRegion: '#main',
    footerRegion: '#footer',
    dialogRegion: '#login-dialog',
    registerRegion: '#register-dialog'
});

LaaS.navigate = function (route, options) {
    options || (options = {});
    Backbone.history.navigate(route, options);
};

toastr.options.positionClass = 'toast-top-center';

LaaS.on('start', function () {
    Backbone.history.start({pushState: true, root: '/laas-server/'});
    Backbone.Intercept.start();
});

LaaS.isRefreshingPage = function () {
    var oldState = sessionStorage.getItem('refresh');
    sessionStorage.setItem('refresh', false);
    return oldState == 'true';
};

_.extend(Marionette.AppRouter.prototype, {
    before: function (route, args) {
        if (LaaS.isRefreshingPage()) {
            if (route != 'login(/)') {
                LaaS.Home.showViewFrame();
                LaaS.scheduleTask(LaaS.Inbox.queryTask);
            }
        }
    }
});

window.onbeforeunload = function () {
    sessionStorage.setItem('refresh', true);
};

LaaS.TaskQueue = [];
LaaS.scheduleTask = function (task) {
    LaaS.TaskQueue.push(task);
    task.timeout = setTimeout(function () {
        task.func();
        if (!task.isOnce) {
            LaaS.stopTask(task);
            task.timeout = setTimeout(task.func, task.interval);
        }
    }, task.interval);
};

LaaS.cleanSessionStorage = function () {
    sessionStorage.removeItem('uid');
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('role');
};

LaaS.resetLayout = function(){
    LaaS.headerRegion.empty();
    LaaS.mainNavRegion.empty();
    LaaS.mainRegion.empty();
    LaaS.footerRegion.empty();
};


LaaS.stopTask = function (task) {
    if (task.timeout) {
        clearTimeout(task.timeout);
    }
};

LaaS.stopTasks = function () {
    var length = LaaS.TaskQueue.length;
    for (var i = 0; i < length; i++) {
        LaaS.stopTask(LaaS.TaskQueue.shift());
    }
};

LaaS.isAdmin = function () {
    var role = sessionStorage.getItem('role');
    return role == 'ROLE_ADMIN';
};

$(document).ajaxError(function (event, xhr, options, thrownError) {
    var statusCode = xhr.status;
    if (statusCode == 401 || statusCode == 403) {
        LaaS.stopTasks();
        LaaS.cleanSessionStorage();
        LaaS.navigate('login', true);
    }
});
