import {inject, Getter} from '@loopback/core';
import {DefaultCrudRepository, repository, HasManyRepositoryFactory} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Event, EventRelations, Pictures} from '../models';
import {PicturesRepository} from './pictures.repository';

export class EventRepository extends DefaultCrudRepository<
  Event,
  typeof Event.prototype.id,
  EventRelations
> {

  public readonly pictures: HasManyRepositoryFactory<Pictures, typeof Event.prototype.id>;

  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource, @repository.getter('PicturesRepository') protected picturesRepositoryGetter: Getter<PicturesRepository>,
  ) {
    super(Event, dataSource);
    this.pictures = this.createHasManyRepositoryFactoryFor('pictures', picturesRepositoryGetter,);
    this.registerInclusionResolver('pictures', this.pictures.inclusionResolver);
  }
}
