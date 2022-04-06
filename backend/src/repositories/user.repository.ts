import {inject, Getter} from '@loopback/core';
import {DefaultCrudRepository, repository, HasOneRepositoryFactory} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {User, UserRelations, UserLocation} from '../models';
import {UserLocationRepository} from './user-location.repository';

export class UserRepository extends DefaultCrudRepository<
  User,
  typeof User.prototype.id,
  UserRelations
> {

  public readonly userLocation: HasOneRepositoryFactory<UserLocation, typeof User.prototype.id>;

  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource, @repository.getter('UserLocationRepository') protected userLocationRepositoryGetter: Getter<UserLocationRepository>,
  ) {
    super(User, dataSource);
    this.userLocation = this.createHasOneRepositoryFactoryFor('userLocation', userLocationRepositoryGetter);
    this.registerInclusionResolver('userLocation', this.userLocation.inclusionResolver);
  }
}
