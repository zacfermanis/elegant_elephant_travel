(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('DealDialogController', DealDialogController);

    DealDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Deal', 'Vendor'];

    function DealDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Deal, Vendor) {
        var vm = this;

        vm.deal = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.vendors = Vendor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.deal.id !== null) {
                Deal.update(vm.deal, onSaveSuccess, onSaveError);
            } else {
                Deal.save(vm.deal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('elegantelephantApp:dealUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, deal) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        deal.image = base64Data;
                        deal.imageContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
