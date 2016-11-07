(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('CustomerDialogController', CustomerDialogController);

    CustomerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Customer', 'Address', 'Vacation', 'QuoteRequest'];

    function CustomerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Customer, Address, Vacation, QuoteRequest) {
        var vm = this;

        vm.customer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.mailingaddresses = Address.query({filter: 'customer-is-null'});
        $q.all([vm.customer.$promise, vm.mailingaddresses.$promise]).then(function() {
            if (!vm.customer.mailingAddress || !vm.customer.mailingAddress.id) {
                return $q.reject();
            }
            return Address.get({id : vm.customer.mailingAddress.id}).$promise;
        }).then(function(mailingAddress) {
            vm.mailingaddresses.push(mailingAddress);
        });
        vm.vacations = Vacation.query();
        vm.quoterequests = QuoteRequest.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.customer.id !== null) {
                Customer.update(vm.customer, onSaveSuccess, onSaveError);
            } else {
                Customer.save(vm.customer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('elegantelephantApp:customerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateOfBirth = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
