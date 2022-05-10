import {
    createStubInstance,
    expect,
    sinon,
    StubbedInstanceWithSinonAccessor,
} from '@loopback/testlab';
import { EventRepository, TicketRepository, TicketRequestRepository, UserRepository } from '../../repositories';
import { TicketRequestController } from '../../controllers';
import { Count } from '@loopback/repository';

describe('TicketRequestController Test', () => {
    let ticketRequestRepository: StubbedInstanceWithSinonAccessor<TicketRequestRepository>;
    let eventRepository: StubbedInstanceWithSinonAccessor<EventRepository>;
    let userRepository: StubbedInstanceWithSinonAccessor<UserRepository>;
    let ticketRepository: StubbedInstanceWithSinonAccessor<TicketRepository>;
    beforeEach(givenStubbedRepository);

    describe('Accept TicketRequest', () => {
        it('Should delete request and create a ticket', async () => {
            const controller = new TicketRequestController(ticketRequestRepository, eventRepository, userRepository, ticketRepository);

            // repository.stubs.find.resolves([{name: 'Pen', slug: 'pen'}]);
            ticketRequestRepository.stubs.deleteAll.resolves({ count: 1 });

            const ticket = await controller.acceptRequest('some_event_id', 'some_user_id');

            sinon.assert.calledOnce(ticketRequestRepository.stubs.deleteAll);
            sinon.assert.calledWithMatch(ticketRepository.stubs.create, {
                event_id: 'some_event_id',
                user_id: 'some_user_id'
            });

            // expect(details).to.containEql({name: 'Pen', slug: 'pen'});
            // sinon.assert.calledWithMatch(repository.stubs.find, {
            //   where: {slug: 'pen'},
            // });
        });
    });

    function givenStubbedRepository() {
        ticketRequestRepository = createStubInstance(TicketRequestRepository);
        eventRepository = createStubInstance(EventRepository);
        userRepository = createStubInstance(UserRepository);
        ticketRepository = createStubInstance(TicketRepository);
      }
});