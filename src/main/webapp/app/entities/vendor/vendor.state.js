(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vendor', {
            parent: 'entity',
            url: '/vendor',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Vendors'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vendor/vendors.html',
                    controller: 'VendorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('vendor-detail', {
            parent: 'entity',
            url: '/vendor/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Vendor'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vendor/vendor-detail.html',
                    controller: 'VendorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Vendor', function($stateParams, Vendor) {
                    return Vendor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vendor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vendor-detail.edit', {
            parent: 'vendor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vendor/vendor-dialog.html',
                    controller: 'VendorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vendor', function(Vendor) {
                            return Vendor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vendor.new', {
            parent: 'vendor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vendor/vendor-dialog.html',
                    controller: 'VendorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                logo: null,
                                logoContentType: null,
                                url: null,
                                phoneNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vendor', null, { reload: 'vendor' });
                }, function() {
                    $state.go('vendor');
                });
            }]
        })
        .state('vendor.edit', {
            parent: 'vendor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vendor/vendor-dialog.html',
                    controller: 'VendorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vendor', function(Vendor) {
                            return Vendor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vendor', null, { reload: 'vendor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vendor.delete', {
            parent: 'vendor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vendor/vendor-delete-dialog.html',
                    controller: 'VendorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Vendor', function(Vendor) {
                            return Vendor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vendor', null, { reload: 'vendor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
