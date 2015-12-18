heights = 5:15;
means_d1 = [49.9,66.4,68.25,87.7,88.9,65.75,91.15,58.7,72.35,59.85,57.4];
means_d2 = [113.45,127.65,138.9,157.7,123.1,195.4,119.2,131.1,161.1,120.65,128.3];
means_d3 = [161.35,158.25,310.2,244.45,408.8,352.0,356.75,261.05,295.3,245.95,185.65];
means_d4 = [668.2,915.6,866.6,1098.5,1490.0,1404.3,1355.3,920.5,899.2,606.2,904.3]/2;
means_d5 = [652.9,1354.8,1326.6,2486.4,2597.9,2527.9,1933.0,2372.9,3075.5,1503.3,851.0]/1.5;

fig = figure;
hold on;
plot(heights, means_d1, '+-', 'LineWidth', 2);
plot(heights, means_d2, '+-', 'LineWidth', 2);
plot(heights, means_d3, '+-', 'LineWidth', 2);
plot(heights, means_d4, '+-', 'LineWidth', 2);
plot(heights, means_d5, '+-', 'LineWidth', 2);
hold off;
xlabel 'Maximum Stack Height'
ylabel 'Time Alive (Mean of 20 simulations)'
legend('Search Depth = 1', 'Search Depth = 2', 'Search Depth = 3', 'Search Depth = 4', 'Search Depth = 5', 'Location', 'northwest')
% saveas(fig, 'height_v_time_dall.pdf');
% close all

depths = 1:5;
mean_times = [48.1,192.4,243.9,598.12,1106.1];
fig = figure;
plot(depths, mean_times, 'r+-', 'LineWidth', 2);
xlabel 'Lookahead Length'
ylabel 'Time Alive (Mean of 20 simulations)'
saveas(fig, 'lookahead_v_time.pdf');
close all
