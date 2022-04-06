import {inject, Getter} from '@loopback/core';
import {DefaultCrudRepository, repository, BelongsToAccessor} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Ticket, TicketRelations, User, Event} from '../models';
import {UserRepository} from './user.repository';
import {EventRepository} from './event.repository';

export class TicketRepository extends DefaultCrudRepository<
  Ticket,
  typeof Ticket.prototype.id,
  TicketRelations
> {

  public readonly User: BelongsToAccessor<User, typeof Ticket.prototype.id>;

  public readonly Event: BelongsToAccessor<Event, typeof Ticket.prototype.id>;

  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource, @repository.getter('UserRepository') protected userRepositoryGetter: Getter<UserRepository>, @repository.getter('EventRepository') protected eventRepositoryGetter: Getter<EventRepository>,
  ) {
    super(Ticket, dataSource);
    this.Event = this.createBelongsToAccessorFor('Event', eventRepositoryGetter,);
    this.registerInclusionResolver('Event', this.Event.inclusionResolver);
    this.User = this.createBelongsToAccessorFor('User', userRepositoryGetter,);
    this.registerInclusionResolver('User', this.User.inclusionResolver);
  }
}
