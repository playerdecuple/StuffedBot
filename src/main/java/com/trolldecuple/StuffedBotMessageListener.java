package com.trolldecuple;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class StuffedBotMessageListener extends ListenerAdapter {

    private final String currentPath = System.getProperty("user.dir");

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();
        Message message = event.getMessage();
        User sender = event.getAuthor();
        JDA jda = event.getJDA();

        String messageContent = message.getContentRaw();
        String[] messageArguments = messageContent.split(" ");

        if (messageArguments.length < 1) return;
        if (sender.isBot()) return;

        if (messageArguments[0].equalsIgnoreCase("!위장")) {

            if (messageArguments.length < 2) {

                String argumentsHelpMessage = "`!위장 [사용자태그]`을 대괄호 없이 작성해 주세요.";
                channel.sendMessage(argumentsHelpMessage).delay(10, TimeUnit.SECONDS).flatMap(Message::delete).queue();

            } else {

                User target = jda.getUserByTag(messageArguments[1]);
                if (target == null) {
                    return;
                }

                if (target.getId().equals(StuffedBot.OWNER)) target = sender;

                String avatarUrl = target.getAvatarUrl();
                String targetName = Objects.requireNonNull(event.getGuild().getMember(target)).getNickname();
                Color mainRoleColor = Objects.requireNonNull(event.getGuild().getMember(target)).getColor();

                if (avatarUrl != null) {
                    try {
                        File tempImage = new File(currentPath + "/tmp/temp.jpg");

                        try {

                            URL url = new URL(avatarUrl);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestProperty("User-Agent", "Mozilla");

                            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                            byte[] buf = new byte[1024];

                            int i;
                            while (-1 != (i = inputStream.read(buf))) {
                                outputStream.write(buf, 0, i);
                            }

                            outputStream.close();
                            inputStream.close();

                            byte[] resultImageByte = outputStream.toByteArray();

                            FileOutputStream fileOutputStream = new FileOutputStream(tempImage.getPath());
                            fileOutputStream.write(resultImageByte);
                            fileOutputStream.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (tempImage.exists()) {
                            try {
                                jda.getSelfUser().getManager().setAvatar(Icon.from(tempImage)).complete();
                            } catch (Exception e) {
                                message.delete().queue();
                                channel.sendMessage("위장을 하려면 조금 기다려야 합니다.").delay(10, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                                return;
                            }
                        }
                    } catch (Exception e) {
                        message.delete().queue();
                        channel.sendMessage("위장을 하려면 조금 기다려야 합니다.").delay(10, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                        return;
                    }
                }

                if (targetName == null) {
                    targetName = target.getName();
                }

                System.out.println("【 위장 완료 】 위장한 태그 : " + targetName + ", 위장 URL : " + avatarUrl + ", 위장 컬러 : " + mainRoleColor);

                event.getGuild().getRoles().get(0).getName();
                Role colorRole = roleNameExists("Color", event.getGuild()) == null ? event.getGuild().createRole().setName("Color").setPermissions(0L).complete() : roleNameExists("Color", event.getGuild());
                event.getGuild().addRoleToMember(Objects.requireNonNull(event.getGuild().getMember(jda.getSelfUser())), Objects.requireNonNull(colorRole)).queue();

                colorRole.getManager().setColor(mainRoleColor).queue();
                Objects.requireNonNull(event.getGuild().getMember(jda.getSelfUser())).modifyNickname(targetName).queue();
                message.delete().queue();

            }

        }

        if (messageArguments[0].equalsIgnoreCase("!전송")) {

            if (messageArguments.length < 2) {
                channel.sendMessage("내용을 입력하세요.").delay(10, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            } else {
                String content = String.join(" ", Arrays.copyOfRange(messageArguments, 1, messageArguments.length));
                channel.sendMessage(content).queue();
            }

            message.delete().queue();

        }

    }

    private Role roleNameExists(String c, Guild guild) {
        List<Role> roleList = guild.getRoles();

        for (Role v : roleList) {
            if (c.equals(v.getName())) {
                return v;
            }
        }

        return null;
    }

}
