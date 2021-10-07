package java.net.Mega2223.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class ElectionPoll {
    public List<Candidate> candidates;
    public Role runFor;

        /*public ElectionPoll(){}
        public ElectionPoll(List<Candidate> Candidates){
            candidates = Candidates;
        }*/

    public ElectionPoll(List<Candidate> Candidates, Role concurredRole) {
        candidates = Candidates;
        runFor = concurredRole;
    }

    private static boolean userEquals(User u1, User u2) {
        return (u1.getId().equals(u2.getId()));
    }

    public List<Candidate> runPoll() {
        List<Candidate> can = candidates;
        Candidate maxVotes = can.get(0);
        for (int g = 1; g < can.size(); g++) {
            Candidate atual = can.get(g);
            if (maxVotes.votes.size() < atual.votes.size()) {
                maxVotes = atual;
            }
        }

        for (int g = 0; g < can.size(); g++) {
            Candidate atual = can.get(g);
            if (atual.votes.size() < maxVotes.votes.size()) {
                can.remove(atual);
            }
        }

        return can;


    }

    public void sortCandidates() {
        List<Candidate> can = candidates;
        List<Candidate> cannot = new ArrayList<Candidate>();

        Candidate maxVotes = can.get(0);
        for (int g = 1; g < can.size(); g++) {
            Candidate atual = can.get(g);
            if (maxVotes.votes.size() < atual.votes.size()) {
                maxVotes = atual;
            }
        }

        for (int voteCount = maxVotes.votes.size(); voteCount >= 0; voteCount--) {
            for (int latas = 0; latas < candidates.size(); latas++) {
                //deve ter um jeito melhor de fazer isso não é possivel
                if (candidates.get(latas).votes.size() == voteCount) {
                    cannot.add(candidates.get(latas));
                }
            }
        }
        candidates = cannot;
        return;
    }

    public void addVote(Candidate candidate, Vote vote) {
        candidate.votes.add(vote);
    }

    public void removeVote(Candidate candidate, Vote vote) {
        candidate.votes.remove(vote);
    }

    public static class Candidate {
        public Member canditdateUser;
        public String candidateIdent;
        public List<Vote> votes;

        public Candidate(Member user, String name) {
            canditdateUser = user;
            candidateIdent = name;

        }


    }

    public class Vote {
        public User voteOwner;
        public Guild voteGuild;
        public Candidate votedFor;

        public Vote(User user, Guild guild, Candidate candidate) {
            voteGuild = guild;
            voteOwner = user;
            votedFor = candidate;
        }
    }
}
