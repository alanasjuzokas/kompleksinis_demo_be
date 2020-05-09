//package bakalauras.demo.adminSpec
//
//import bakalauras.demo.controller.AdminController
//import bakalauras.demo.db.PollRepository
//import bakalauras.demo.db.RequestRepository
//import bakalauras.demo.entities.Poll
//import bakalauras.demo.entities.domain.PollStatus
//import bakalauras.demo.web.domain.PollRequest
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import spock.lang.Specification
//
//class adminControllerSpec extends Specification {
//
//    private final RequestRepository requestRepository = Mock(RequestRepository)
//    private final PollRepository pollRepository = Mock(PollRepository)
//
//    private final AdminController controller = new AdminController(requestRepository, pollRepository)
//
//    private Poll poll = new Poll([
//            id: '1',
//            status: PollStatus.STARTED,
//            choices: ['pirmmas','antras'],
//            requesterId: '2'
//    ])
//
//    private PollRequest request = new PollRequest([ pollId: '1'])
//
//    def "stops running poll"() {
//        given:
//        pollRepository.findById(_) >> Optional.of(poll)
//
//        when:
//        ResponseEntity response = controller.stopPoll(request)
//
//        then:
//        poll.status == PollStatus.STOPPED
//        response.statusCode == HttpStatus.OK
//    }
//
//    def "returns 404 if such poll does not exists"() {
//        given:
//        pollRepository.findById(_) >> Optional.empty()
//
//        when:
//        ResponseEntity response = controller.stopPoll(request)
//
//        then:
//        response.statusCode == HttpStatus.NOT_FOUND
//    }
//
//    def "returns status ok and does not change status if poll is stopped"() {
//        given:
//        poll.status = PollStatus.STOPPED
//        pollRepository.findById(_) >> Optional.of(poll)
//
//        when:
//        ResponseEntity response = controller.stopPoll(request)
//
//        then:
//        poll.status == PollStatus.STOPPED
//        response.statusCode == HttpStatus.TEMPORARY_REDIRECT
//    }
//
//}
