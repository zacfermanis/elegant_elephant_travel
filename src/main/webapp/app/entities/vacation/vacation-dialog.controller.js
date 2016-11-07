(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('VacationDialogController', VacationDialogController);

    VacationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Vacation', 'Card', 'Quote', 'Passenger', 'Customer'];

    function VacationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Vacation, Card, Quote, Passenger, Customer) {
        var vm = this;

        vm.vacation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.cards = Card.query({filter: 'vacation-is-null'});
        $q.all([vm.vacation.$promise, vm.cards.$promise]).then(function() {
            if (!vm.vacation.card || !vm.vacation.card.id) {
                return $q.reject();
            }
            return Card.get({id : vm.vacation.card.id}).$promise;
        }).then(function(card) {
            vm.cards.push(card);
        });
        vm.quotes = Quote.query();
        vm.passengers = Passenger.query();
        vm.customers = Customer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vacation.id !== null) {
                Vacation.update(vm.vacation, onSaveSuccess, onSaveError);
            } else {
                Vacation.save(vm.vacation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('elegantelephantApp:vacationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.departureDate = false;
        vm.datePickerOpenStatus.returnDate = false;

        vm.setSignature = function ($file, vacation) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        vacation.signature = base64Data;
                        vacation.signatureContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
