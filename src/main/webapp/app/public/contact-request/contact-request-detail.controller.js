(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('ContactRequestDetailController', ContactRequestDetailController);

    ContactRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ContactRequest'];

    function ContactRequestDetailController($scope, $rootScope, $stateParams, previousState, entity, ContactRequest) {
        var vm = this;

        vm.contactRequest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('elegantelephantApp:contactRequestUpdate', function(event, result) {
            vm.contactRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
