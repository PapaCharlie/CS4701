heights = 5:15;
means_d4 = [668.2,915.6,866.6,1098.5,1490.0,1404.3,1355.3,920.5,899.2,606.2,904.3]/2;
fig = figure;
plot(heights, means_d4, 'r+-', 'LineWidth', 2);
xlabel 'Maximum Stack Height'
ylabel 'Consecutive Tetrominoes Placed'
saveas(fig, 'height_v_time_d4.eps');
close all

depths = 1:4;
mean_times = [48.1,210.65,215.8,814.45];
fig = figure;
plot(depths, mean_times, 'r+-', 'LineWidth', 2);
xlabel 'Lookahead'
ylabel 'Consecutive Tetrominoes Placed'
saveas(fig, 'lookahead_v_time.eps');
close all
