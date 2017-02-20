package api;

import com.alibaba.fastjson.JSON;
import entity.Track;
import exception.http.BadRequestException;
import exception.http.UnauthorizedException;
import exception.http.UnknownException;
import exception.service.ExecuteException;
import exception.service.ForbiddenException;
import exception.service.IllegalInputException;
import exception.service.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.TrackService;
import utilities.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by fan on 7/19/2016.
 */
@RestController
@RequestMapping(value = "/track")
public class TrackResource {
    private static final Logger logger = LogManager.getLogger();
    private static final TrackService trackService = SpringIocUtil.getBean("trackService", TrackService.class);
    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String createTrack(@RequestBody String reqTrack) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        Track newTrack = null;
        try {
            newTrack = JSON.parseObject(reqTrack, Track.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("json error: " + e.getMessage());
            throw new BadRequestException("createTrack illegal input: " + e.getMessage());
        }

        try {
            newTrack = trackService.addTrack(token, newTrack);
        } catch (IllegalInputException e) {
            logger.debug("illegal request body: " + e.getMessage());
            throw new exception.http.BadRequestException("illegal request body: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("create track error: " + e.getMessage());
        }

        return JSON.toJSONString(newTrack);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listMyTrack() {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        String startParam = request.getParameter("start");
        String countParam = request.getParameter("count");
        String optionParam = request.getParameter("option");

        int start = 0;
        int count = 5;
        int option = 0;
        if (startParam != null && !startParam.equals("")) {
            start = Integer.valueOf(startParam);
        }
        if (countParam != null && !countParam.equals("")) {
            count = Integer.valueOf(countParam);
            count = count > 50 ? 50 : count;
        }
        if (optionParam != null && !optionParam.equals("")) {
            int tmp = Integer.valueOf(optionParam);
            if (tmp < 0 || tmp > 2) {
                throw new BadRequestException("illegal query: option");
            }
            option = tmp;
        }

        List<Track> tracks = null;

        try {
            tracks = trackService.listUsersTrack(token, option, start, count);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("list my track error: " + e.getMessage());
        }

        return JSON.toJSONString(tracks);
    }

    @RequestMapping(value = "/fork/{track_id}", method = RequestMethod.POST)
    public String forkTrack(@PathVariable String track_id) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        Track forkedTrack = null;

        try {
            forkedTrack = trackService.forkTrack(token, track_id);
        } catch (NotFoundException e) {
            logger.debug("track not found when fork: " + e.getMessage());
            throw new exception.http.NotFoundException("track not found when fork: " + e.getMessage());
        } catch (ForbiddenException e) {
            logger.debug("fork forbidden: " + e.getMessage());
            throw new exception.http.ForbiddenException("fork forbidden: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("fork track error: " + e.getMessage());
        }

        return JSON.toJSONString(forkedTrack);
    }

    @RequestMapping(value = "/star/{track_id}", method = RequestMethod.POST)
    public String starTrack(@PathVariable String track_id) {


        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            trackService.starTrack(token, track_id);
        } catch (ForbiddenException e) {
            logger.debug("star forbidden: " + e.getMessage());
            throw new exception.http.ForbiddenException("star forbidden: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("track not found when star: " + e.getMessage());
            throw new exception.http.NotFoundException("track not found when star: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("star track error: " + e.getMessage());
        }

        return "";
    }

    @RequestMapping(value = "/pullrequest/{my_track_id}", method = RequestMethod.POST)
    public String pullRequest(@PathVariable String my_track_id) {


        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            trackService.pullRequest(token, my_track_id);
        } catch (ForbiddenException e) {
            logger.debug("pullRequest forbidden: " + e.getMessage());
            throw new exception.http.ForbiddenException("pull request yourself: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("track not found when pull request: " + e.getMessage());
            throw new exception.http.NotFoundException("track not found when pull request: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("pull request error: " + e.getMessage());
        }

        return "";
    }

    @RequestMapping(value = "/{track_id}", method = RequestMethod.DELETE)
    public String deleteTrack(@PathVariable String track_id) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            trackService.deleteTrack(token, track_id);
        } catch (NotFoundException e) {
            logger.debug("track not found when delete: " + e.getMessage());
            throw new exception.http.NotFoundException("track not found when delete: " + e.getMessage());
        } catch (ForbiddenException e) {
            logger.debug("not your track: " + e.getMessage());
            throw new exception.http.ForbiddenException("not your track: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("delete track error: " + e.getMessage());
        }


        return "";
    }

    @RequestMapping(value = "/{track_id}", method = RequestMethod.PUT)
    public String editTrack(@PathVariable String track_id, @RequestBody String reqTrack) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        Track updatedTrack = null;
        try {
            updatedTrack = JSON.parseObject(reqTrack, Track.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("json error: " + e.getMessage());
            throw new BadRequestException("illegal input: " + e.getMessage());
        }

        try {
            updatedTrack = trackService.editTrack(token, track_id, updatedTrack);
        } catch (IllegalInputException e) {
            logger.debug("bad request when editing a track: " + e.getMessage());
            throw new BadRequestException("bad request when editing a track: " + e.getMessage());
        } catch (ForbiddenException e) {
            logger.debug("not your track: " + e.getMessage());
            throw new exception.http.ForbiddenException("not your track: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("track not found when editing a track: " + e.getMessage());
            throw new exception.http.NotFoundException("track not found when editing a track: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("edit track error: " + e.getMessage());
        }

        return JSON.toJSONString(updatedTrack);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchTracks() {

        String categoryParam = request.getParameter("category_ref");
        String titleParam = request.getParameter("title");
        String startParam = request.getParameter("start");
        String countParam = request.getParameter("count");

        int start = 0;
        int count = 20;
        Integer categoryId = null;
        String title = null;
        try {
            if (startParam != null && !startParam.equals("")) {
                start = Integer.valueOf(startParam);
            }
            if (countParam != null && !countParam.equals("")) {
                count = Integer.valueOf(countParam);
                count = count > 100 ? 100 : count;
            }
            if (categoryParam != null && !categoryParam.equals("")) {
                categoryId = Integer.valueOf(categoryParam);
            }
        } catch (NumberFormatException e) {
            logger.debug("bad query when search: " + e.getMessage());
            throw new BadRequestException("bad query format when search: " + e.getMessage());
        }

        if (titleParam != null && !titleParam.equals("")) {
            title = titleParam;
        }

        List<Track> results = null;

        try {
            results = trackService.searchTracks(categoryId, title, start, count);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("search track error: " + e.getMessage());
        }

        return JSON.toJSONString(results);
    }

    @RequestMapping(value = "/{track_id}", method = RequestMethod.GET)
    public String showTrack(@PathVariable String track_id) {

        Track track = null;


        try {
            track = trackService.showTrack(track_id);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundException("track not found: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("show track error: " + e.getMessage());
        }


        return JSON.toJSONString(track);
    }


}
